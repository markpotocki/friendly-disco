package dev.homely.filemanager.quotas;

import reactor.core.publisher.Mono;

public interface QuotaService {

    Mono<UserQuota> incrementUsedMemory(String userId, Long amount);

    Mono<UserQuota> decrementUsedMemory(String userId, Long amount);

    Mono<UserQuota> changeTotalMemory(String userId, Long amountOfBytes);

    @Deprecated
    Mono<Boolean> isFull(String userId);

    Mono<Boolean> isFull(String userId, Long contentLength);
}
