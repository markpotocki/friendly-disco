package dev.homely.filemanager.upload.database;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface FileEventRepository extends ReactiveMongoRepository<FileEvent, String> {
    public Flux<FileEvent> getByUploaderId(String uploaderId);
}
