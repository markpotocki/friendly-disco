package dev.homely.filemanager.quotas;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserQuotaRepository extends ReactiveMongoRepository<UserQuota, String> {
    Mono<UserQuota> findByUserId(String userId);
}
