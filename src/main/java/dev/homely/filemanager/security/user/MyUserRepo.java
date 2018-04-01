package dev.homely.filemanager.security.user;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MyUserRepo extends ReactiveMongoRepository<MyUser, String> {
    Mono<MyUser> findByUsername(String username);
}
