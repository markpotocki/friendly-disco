package dev.homely.filemanager.upload.database;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends ReactiveMongoRepository<FileLog, String> {
    Flux<FileLog> getByUploader(String uploader);
    Mono<FileLog> getByUploaderAndId(String uploader, String id);
}
