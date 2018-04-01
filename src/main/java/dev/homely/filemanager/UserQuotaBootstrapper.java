package dev.homely.filemanager;

import dev.homely.filemanager.quotas.UserQuota;
import dev.homely.filemanager.quotas.UserQuotaRepository;
import dev.homely.filemanager.upload.database.FileLog;
import dev.homely.filemanager.upload.database.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserQuotaBootstrapper implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(UserQuotaBootstrapper.class);
    @Autowired
    private UserQuotaRepository quotaRepository;
    @Autowired
    private FileRepository fileRepository;
    @Override
    public void run(String... args) throws Exception {
        log.warn("Running Quota Bootstrapper");
        quotaRepository.save(UserQuota.builder().userId("user").memoryAmount(0L).memoryLimit(25000000L).build()).subscribe();
        log.warn("Running file rep bootstrap");
        fileRepository.save(FileLog.builder().uploader("user").fileName("foobar").filePath("foobar").size(1L).build()).subscribe();
    }
}
