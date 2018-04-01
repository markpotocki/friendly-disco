package dev.homely.filemanager.filesdisplay;

import dev.homely.filemanager.upload.database.FileLog;
import dev.homely.filemanager.upload.database.FileEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileInfoService {

    /**
     * Used to get the Document FileLog for returning the file info. Should check against
     * the userId so that a user can only access their own files.
     * @param userId id of the user to find the files for
     * @return a Flux of the files from the user
     */
    Flux<FileLog> getFiles(String userId);

    Mono<FileLog> getFile(String userId, String fileId);

    Flux<FileEvent> getAllFileEvents();

    Flux<FileEvent> getFileEventsByUser(String userId);

    Mono<Void> deleteFile(String userId, String fileId);

}
