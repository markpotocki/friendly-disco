package dev.homely.filemanager.filesdisplay;

import dev.homely.filemanager.upload.database.FileLog;
import dev.homely.filemanager.upload.database.FileEvent;
import dev.homely.filemanager.upload.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileInfoService fileService;

    @Autowired
    private StorageService storageService;

    @GetMapping
    public Flux<FileLog> getFiles(@AuthenticationPrincipal Principal principal) {
        return fileService
                .getFiles(principal.getName())
                .switchIfEmpty(Flux.error(new FileNotFoundException("Files for user " /*+ principal.getName()*/ + " not found.")));
    }

    @GetMapping("/{fileId}")
    public Mono<FileLog> getFile(Principal principal, @PathVariable("fileId")String fileId) {
        return fileService
                .getFile(principal.getName(), fileId);
    }

    @PreAuthorize("hasRole(ADMIN)")
    @GetMapping("/admin/events")
    public Flux<FileEvent> getAllFileEvents() {
        return fileService
                .getAllFileEvents();
    }

    @GetMapping("/events")
    public Flux<FileEvent> getFileEventsForUser(Principal principal) {
        return fileService
                .getFileEventsByUser(principal.getName());
    }

    @DeleteMapping("/{fileId}")
    public Mono<Void> deleteOneFile(Principal principal, @PathVariable("fileId") String fileId) {
        log.warn("FILE Id in path: " + fileId);
        log.warn("USER: " + principal.getName());

        return fileService
                .getFile(principal.getName(), fileId).log("delete-get-file")
                .map( fileLog -> fileLog.getFileName()).log("delete-map-string")
                .flatMap( fileName -> {
                    Mono<Void> sService = storageService.delete(fileName).log("delete-storage");
                    Mono<Void> fService = fileService.deleteFile(principal.getName(), fileId).log("delete-db");
                    return Mono.when(sService, fService);
                }).then();
    }

}
