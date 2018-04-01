package dev.homely.filemanager.upload;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.homely.filemanager.quotas.MaximumStorageExceededException;
import dev.homely.filemanager.quotas.QuotaService;
import dev.homely.filemanager.upload.storage.StorageService;
import io.netty.handler.codec.CodecException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.Duration;

@RestController
@RequestMapping("/api/file")
@NoArgsConstructor
public class UploadController {

    // pads the file because content length is a little smaller than the actual file size
    private final static Long FILE_PADDING_BYTES = 1000L;

    private final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private StorageService storageService;

    @Autowired
    private QuotaService quotaService;

    @PostMapping
    public Mono<Void> save(@RequestPart("file") MultipartFile file) {
        log.info("Storing a new file. Recieved by Controller");
        this.storageService.store(file);
        return Mono.empty();
        //file.log().subscribe( (f) -> this.storageService.store(f));
    }

    @PostMapping("/test")
    public Mono<Void> saveg(@AuthenticationPrincipal Principal principal, @RequestPart("file")Flux<FilePart> filePart, ServerWebExchange request) {
        Long fileSize = request.getRequest().getHeaders().getContentLength() + FILE_PADDING_BYTES;

        if (quotaService.isFull(principal.getName(), fileSize).block(Duration.ofSeconds(10)))
            return storageService.store(principal.getName(), filePart, fileSize);
        else
            return Mono.error(new MaximumStorageExceededException("Storage exceeded"));
    }

    @ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
    @ExceptionHandler(MaximumStorageExceededException.class)
    public Mono<Void> handleStorageExceeded() {
        return Mono.empty();
    }

}
