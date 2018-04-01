package dev.homely.filemanager.quotas;

public class QuotaNotExistException extends RuntimeException {

    QuotaNotExistException(String message) {
        super(message);
    }

    QuotaNotExistException(String message, Throwable t) {
        super(message, t);
    }
}
