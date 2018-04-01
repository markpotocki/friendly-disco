package dev.homely.filemanager.upload.storage;

import dev.homely.filemanager.quotas.QuotaService;
import dev.homely.filemanager.quotas.UserQuotaRepository;
import dev.homely.filemanager.upload.database.FileLog;
import dev.homely.filemanager.upload.database.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.stream.Stream;


@Service
public class BasicStorageService implements StorageService {

    private final Path rootLocation;
    private final Logger log = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private QuotaService quotaService;

    @Autowired
    public BasicStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public Mono<Void> store(String userId, Flux<FilePart> fileParts, Long fileSize) {
        return fileParts
                .log("storing started")
                .flatMap(filePart -> {
                    Mono<FileLog> saveFile = fileRepository.save(FileLog.builder().fileName(filePart.filename())
                            .uploader(userId).size(fileSize).filePath(filePart.filename()).build()).log("file-save");

                    Mono<Void> copyFile = Mono.just(this.rootLocation.resolve(filePart.filename()).toFile())
                            .log("choose-location")
                            .map(destFile -> {
                                try {
                                    log.info(destFile.getAbsolutePath());
                                    destFile.createNewFile();
                                    return destFile;
                                } catch(IOException e) {
                                    throw new StorageException("Could not save file correctly.", e);
                                }
                            }).log("created-file")
                            .flatMap(filePart::transferTo)
                            .log("Copied file correctly")
                            .then(Mono.fromRunnable(() -> {
                                log.warn("File size is " + this.rootLocation.resolve(filePart.filename()).toFile().length());
                                quotaService.incrementUsedMemory(userId, this.rootLocation.resolve(filePart.filename()).toFile().length()).log();
                            }));
                    return Mono.when(saveFile, copyFile).log("save-file-when-event");
                }).log("large flatmap").then();
    }



    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty())
                throw new StorageException("FileLog cannot be an empty file");
            if (filename.contains(".."))
                throw new StorageException("Invalid file name. A file cannot start with '../'");
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Could not connect to storage filesystem", e);
        }
    }

    @Override
    public Mono<Path> load(String filename) {
        return Mono.just(rootLocation.resolve(filename));
    }

    @Override
    public Mono<Resource> loadAsResource(String filename) {
        return load(filename)
                .map( x-> x.toUri() )
                .flatMap(uri -> Mono.just(this.checkUrlResource(uri)))
                .cast(Resource.class)
                .filter( resource ->  resource.exists() || resource.isReadable() );

    }

    private UrlResource checkUrlResource(URI uri) {
        try {
            return new UrlResource(uri);
        }
        catch (MalformedURLException e) {
            throw new StorageException("Resource name is invalid", e);
        }
    }

    @Override
    public Flux<Path> loadAll() {
        return Flux.fromStream(this::safeConstruct)
                .filter(path -> !path.equals(this.rootLocation))
                .map(path -> this.rootLocation.relativize(path));
    }

    private Stream<Path> safeConstruct() {
        try {
            return Files.walk(this.rootLocation, 1);
        }
        catch (IOException e) {
            throw new StorageException("IOException in loadall", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Mono<Void> delete(String fileName) {
        return load(fileName)
                .log("deleteing file")
                .map(foo -> foo.toFile())
                .doOnNext(FileSystemUtils::deleteRecursively)
                .log("files have been deleted")
                .then();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch(IOException e) {
            throw new StorageException("Failed to initialize Storage Service", e);
        }
    }
}
