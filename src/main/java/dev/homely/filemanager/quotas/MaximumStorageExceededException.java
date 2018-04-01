package dev.homely.filemanager.quotas;

public class MaximumStorageExceededException extends RuntimeException {

    public MaximumStorageExceededException(String message) {
        super(message);
    }
}
