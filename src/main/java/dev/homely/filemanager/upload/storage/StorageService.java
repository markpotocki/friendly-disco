package dev.homely.filemanager.upload.storage;


import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;

public interface StorageService {

    void init();

    @Deprecated
    void store(MultipartFile file);

    Mono<Void> store(String userId, Flux<FilePart> filePart, Long fileSize);

    Flux<Path> loadAll();

    Mono<Path> load(String filename);

    Mono<Resource> loadAsResource(String filename);

    void deleteAll();

    Mono<Void> delete(String file);
}
