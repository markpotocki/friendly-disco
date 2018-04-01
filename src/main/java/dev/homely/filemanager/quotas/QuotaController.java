package dev.homely.filemanager.quotas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/quotas")
public class QuotaController {

    @Autowired
    private UserQuotaRepository quotaRepository;

    @GetMapping("/{userId}")
    public Mono<UserQuota> getUsersQuota(@PathVariable("userId") String userId) {
        return quotaRepository
                .findByUserId(userId)
                .switchIfEmpty(Mono.error(new QuotaNotExistException("Could not find quota for this user")));
    }

    @GetMapping
    public Flux<UserQuota> getQuotas() {
        return quotaRepository
                .findAll();
    }

}
