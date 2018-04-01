package dev.homely.filemanager.filesdisplay;

import dev.homely.filemanager.quotas.QuotaService;
import dev.homely.filemanager.upload.database.FileLog;
import dev.homely.filemanager.upload.database.FileEvent;
import dev.homely.filemanager.upload.database.FileEventRepository;
import dev.homely.filemanager.upload.database.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileEventRepository fileEventRepository;
    @Autowired
    private QuotaService quotaService;

    @Override
    public Flux<FileLog> getFiles(String userId) {
        return fileRepository.getByUploader(userId);
    }

    @Override
    public Mono<FileLog> getFile(String userId, String fileId) {
        return fileRepository
                .findById(fileId)
                .filter( file -> file.getUploader().equals(userId))
                .switchIfEmpty(Mono.error(new FileNotFoundException(fileId + " was not found.")));
    }

    @Override
    public Mono<Void> deleteFile(String userId, String fileId) {
        log.warn(fileRepository.getByUploaderAndId(userId, fileId).toString());
        return fileRepository
                .getByUploaderAndId(userId, fileId).log("find-file")
                //.switchIfEmpty(Mono.error(new RuntimeException()))
                .doOnNext( fileLog -> log.warn("Db Query Results: " + fileLog.toString()))
                .flatMap( fileLog -> {
                    Mono<Void> qService = quotaService.decrementUsedMemory(userId, fileLog.getSize()).log("decrement quota").then();
                    Mono<Void> fRepo = fileRepository.delete(fileLog).log("delete file");
                    return Mono.when(qService, fRepo);

                });
    }

    @Override
    public Flux<FileEvent> getAllFileEvents() {
        return fileEventRepository.findAll();
    }

    @Override
    public Flux<FileEvent> getFileEventsByUser(String userId) {
        return fileEventRepository.getByUploaderId(userId);
    }
}
