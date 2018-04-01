package dev.homely.filemanager.filedisplay;

import dev.homely.filemanager.upload.database.*;
import dev.homely.filemanager.filesdisplay.FileInfoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FileInfoServiceTest {

    @InjectMocks
    private FileInfoServiceImpl fileService = new FileInfoServiceImpl();

    @Mock
    private FileRepository mockFileRepo;

    @Mock
    private FileEventRepository mockEventRepo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(FileInfoServiceTest.class);
    }

    @Test
    public void setupCorrectly() {
        assertNotNull(mockFileRepo);
        assertNotNull(mockEventRepo);
    }

    @Test
    public void getAllFilesWithValidUser() {
        Flux<FileLog> mock = Flux.just(FileLog.builder().fileName("bar").filePath("bar").uploader("foo").build());
        given(this.mockFileRepo.getByUploader("foo")).willReturn(mock);
        Flux<FileLog> result = fileService.getFiles("foo");

        assertFluxEquals(mock, result);

        verify(mockFileRepo).getByUploader("foo");
    }

    @Test
    public void getCorrectFile() {
        Mono<FileLog> mock = Mono.just(FileLog.builder().uploader("foo").id("1e").filePath("bar").fileName("bar").build());
        given(this.mockFileRepo.findById("1e"))
                .willReturn(mock);
        Mono<FileLog> result = fileService.getFile("foo", "1e");
        assertMonoEquals(mock, result);
        verify(mockFileRepo).findById("1e");
    }

    @Test
    public void getAllFileEvents() {
        Flux<FileEvent> events = Flux.just(
                FileEvent.builder().uploaderId("foo").fileId("foo").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().uploaderId("foo").fileId("foo").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().uploaderId("foo").fileId("foo").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().uploaderId("foo").fileId("foo").actionType(ActionType.RESTRICT).build()
        );

        given(this.mockEventRepo.findAll()).willReturn(events);
        Flux<FileEvent> actual = this.fileService.getAllFileEvents();

        assertFluxEquals(events, actual);

        verify(this.mockEventRepo).findAll();

    }


    static void assertMonoEquals(Mono<?> expected, Mono<?> actual) {
        assertEquals(expected.block(), actual.block());
    }

    static void assertFluxEquals(Flux<?> s1, Flux<?> s2)
    {
        Iterator<?> iter1 = s1.toIterable().iterator(), iter2 = s2.toIterable().iterator();
        while(iter1.hasNext() && iter2.hasNext())
            assertEquals(iter1.next(), iter2.next());
        assert !iter1.hasNext() && !iter2.hasNext();
    }
}
