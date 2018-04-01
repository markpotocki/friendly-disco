package dev.homely.filemanager.upload.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFoundException extends RuntimeException {

    public StorageNotFoundException(String filename) {
        super("FileLog " + filename + "was not found");
    }

    public StorageNotFoundException(String filename, Throwable cause) {
        super(filename, cause);
    }
}
