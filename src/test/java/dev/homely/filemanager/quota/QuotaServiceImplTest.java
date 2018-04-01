package dev.homely.filemanager.quota;


import dev.homely.filemanager.quotas.MaximumStorageExceededException;
import dev.homely.filemanager.quotas.QuotaServiceImpl;
import dev.homely.filemanager.quotas.UserQuota;
import dev.homely.filemanager.quotas.UserQuotaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuotaServiceImplTest {

    @InjectMocks
    private QuotaServiceImpl quotaService = new QuotaServiceImpl();

    @Mock
    private UserQuotaRepository quotaRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(QuotaServiceImplTest.class);
    }

    @Test
    public void testSetup() {
        assertNotNull(quotaService);
        assertNotNull(quotaRepository);
    }

    @Test
    public void incrementUserQuotaBy10() {
        // setup test
        Mono<UserQuota> inital = Mono.just(UserQuota.builder().userId("user").memoryAmount(0L).memoryLimit(100L).build());
        Mono<UserQuota> expected = Mono.just(UserQuota.builder().userId("user").memoryAmount(10L).memoryLimit(100L).build());

        when(quotaRepository.findById("user")).thenReturn(inital);
        when(quotaRepository.save(inital.block())).thenReturn(expected);

        // test step
        Mono<UserQuota> actual = quotaService.incrementUsedMemory("user", 10L);

        // verification
        assertEquals(expected.block(), actual.block());

        verify(quotaRepository).findById("user");
        verify(quotaRepository).save(inital.block());
    }

    @Test(expected = MaximumStorageExceededException.class)
    public void incrementQuotaOverLimit_ThrowException() {
        // setup test
        Mono<UserQuota> inital = Mono.just(UserQuota.builder().userId("user").memoryAmount(0L).memoryLimit(9L).build());
        Mono<UserQuota> expected = Mono.just(UserQuota.builder().userId("user").memoryAmount(10L).memoryLimit(9L).build());

        when(quotaRepository.findById("user")).thenReturn(inital);

        // test step
        Mono<UserQuota> actual = quotaService.incrementUsedMemory("user", 10L);

        // verification
        assertEquals(inital.block(), actual.block()); // should revert back to inital

        verify(quotaRepository).findById("user");
        verify(quotaRepository).save(inital.block());
    }

    @Test
    public void decrementUserQuotaBy10() {
        // setup test
        Mono<UserQuota> inital = Mono.just(UserQuota.builder().userId("user").memoryAmount(10L).memoryLimit(100L).build());
        Mono<UserQuota> expected = Mono.just(UserQuota.builder().userId("user").memoryAmount(0L).memoryLimit(100L).build());

        when(quotaRepository.findById("user")).thenReturn(inital);
        when(quotaRepository.save(inital.block())).thenReturn(expected);

        // test step
        Mono<UserQuota> actual = quotaService.decrementUsedMemory("user", 10L);

        // verification
        assertEquals(expected.block(), actual.block());

        verify(quotaRepository).findById("user");
        verify(quotaRepository).save(inital.block());
    }

    @Test
    public void changeUserQuotaLimit() {
        // setup test
        Mono<UserQuota> inital = Mono.just(UserQuota.builder().userId("user").memoryAmount(0L).memoryLimit(100L).build());
        Mono<UserQuota> expected = Mono.just(UserQuota.builder().userId("user").memoryAmount(0L).memoryLimit(1000L).build());

        when(quotaRepository.findById("user")).thenReturn(inital);
        when(quotaRepository.save(inital.block())).thenReturn(expected);

        // test step
        Mono<UserQuota> actual = quotaService.changeTotalMemory("user", 1000L);

        // verification
        assertEquals(expected.block(), actual.block());

        verify(quotaRepository).findById("user");
        verify(quotaRepository).save(inital.block());
    }
}
