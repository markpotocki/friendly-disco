package dev.homely.filemanager.quotas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class QuotaServiceImpl implements QuotaService {

    @Autowired
    private UserQuotaRepository quotaRepository;

    @Override
    public Mono<UserQuota> incrementUsedMemory(String userId, Long amountOfBytes) {
        log.warn("STARTING INCREMENT of used memory " + userId + " : " + amountOfBytes);
        Mono<UserQuota> ret = quotaRepository
                .findByUserId(userId)
                .log("userId finding")
                .switchIfEmpty(Mono.error(new QuotaNotExistException("Quota data doesn't exist for the user")))
                .log("switchIfEmpty-userid")
                .filter( userQuota -> userQuota.getMemoryAmount() + amountOfBytes < userQuota.getMemoryLimit())
                .switchIfEmpty(Mono.error(new MaximumStorageExceededException("You have exceeded your allocated storage.")))
                .log("switchIfEmpty-freespace")
                .map( userQuota -> {
                    log.info("userQuota: " + userQuota.toString());
                    userQuota.setMemoryAmount(userQuota.getMemoryAmount() + amountOfBytes);
                    log.info("ChangeQuota: " + userQuota.toString());
                    return userQuota;
                })
                .flatMap( userQuota -> quotaRepository.save(userQuota)).log("DoNext-save");
        ret.subscribe();
        return ret;
    }

    @Override
    public Mono<UserQuota> decrementUsedMemory(String userId, Long amountOfBytes) {
        log.warn("STARTING INCREMENT of used memory " + userId + " : " + amountOfBytes);
        Mono<UserQuota> q =  quotaRepository
                .findByUserId(userId)
                .switchIfEmpty(Mono.error(new QuotaNotExistException("Quota data doesn't exist for the user")))
                .map( userQuota -> {
                    userQuota.setMemoryAmount(userQuota.getMemoryAmount() - amountOfBytes);
                    return userQuota;
                })
                .flatMap( userQuota -> quotaRepository.save(userQuota));
        q.subscribe();
        return q;
    }

    @Override
    public Mono<UserQuota> changeTotalMemory(String userId, Long newAmount) {
        return quotaRepository
                .findByUserId(userId)
                .switchIfEmpty(Mono.error(new QuotaNotExistException("Quota data does not exist")))
                .map( userQuota -> {
                    userQuota.setMemoryLimit(newAmount);
                    return userQuota;
                })
                .flatMap( userQuota -> quotaRepository.save(userQuota));
    }

    @Override
    public Mono<Boolean> isFull(String userId) {
        return quotaRepository.findByUserId(userId)
                .filter(quota -> quota.getMemoryAmount() < quota.getMemoryLimit())
                .map(quota -> quota != null);
    }

    @Override
    public Mono<Boolean> isFull(String userId, Long contentLength) {
        return quotaRepository.findByUserId(userId)
                .filter(quota -> quota.getMemoryAmount() + contentLength < quota.getMemoryLimit())
                .switchIfEmpty(Mono.error(new MaximumStorageExceededException("Quota has been exceeded. Please get more space or consider deleting your other files.")))
                .map(quota -> quota != null);
    }
}
