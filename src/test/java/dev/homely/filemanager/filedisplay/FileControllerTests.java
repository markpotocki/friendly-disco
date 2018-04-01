package dev.homely.filemanager.filedisplay;


import dev.homely.filemanager.upload.database.ActionType;
import dev.homely.filemanager.upload.database.FileLog;
import dev.homely.filemanager.upload.database.FileEvent;
import dev.homely.filemanager.filesdisplay.FileInfoService;
import dev.homely.filemanager.filesdisplay.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class FileControllerTests {

    @MockBean
    private FileInfoService fileService;

    private WebTestClient client;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setup() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void testSetup() {
        assertNotNull(client);
        assertNotNull(fileService);
        assertNotNull(context);
    }

    @Test
    @WithMockUser
    public void getAllFiles() {
        Flux<FileLog> files = Flux.just(
                FileLog.builder().fileName("foo").filePath("bar").uploader("user").build()
        );

        when(fileService.getFiles("user")).thenReturn(files);

        FileLog[] expected = files.collectList().block().toArray(new FileLog[1]);

        client.get()
                .uri("/api/files")
                .exchange()
                .expectStatus()
                    .is2xxSuccessful()
                .expectBody(FileLog[].class)
                    .isEqualTo(expected) ;
        verify(fileService).getFiles("user");
    }

    @Test
    @WithMockUser
    public void getAllFilesWithNone_Throw404() {
        when(fileService.getFiles("user")).thenThrow(new FileNotFoundException("file not found"));

        client.get()
                .uri("/api/files")
                .exchange()
                .expectStatus()
                    .is4xxClientError();

        verify(fileService).getFiles("user");

    }

    @Test
    @WithMockUser
    public void getOneFile() {
        Mono<FileLog> expected = Mono.just(FileLog.builder().filePath("blah").uploader("user").id("1e").fileName("yeua").build());
        when(fileService.getFile("user", "1e")).thenReturn(expected);

        client.get()
                .uri("/api/files/1e")
                .exchange()
                .expectStatus()
                    .is2xxSuccessful()
                .expectBody(FileLog.class)
                    .isEqualTo(expected.block());

        verify(fileService).getFile("user", "1e");
    }

    @Test
    @WithMockUser
    public void getOneFileNotExist_Throw404() {
        when(fileService.getFile("user", "1e")).thenThrow(new FileNotFoundException("na"));

        client.get()
                .uri("/api/files/1e")
                .exchange()
                .expectStatus()
                    .isNotFound();
        verify(fileService).getFile("user", "1e");
    }

    @Test
    public void getFilesNoAuthorized_Throw401() {
        client.get()
                .uri("/api/files/1e")
                .exchange()
                .expectStatus()
                    .isUnauthorized();
    }

    @Test
    @WithMockUser( value="admin", roles = { "ADMIN" })
    public void getFileEventsAllAsAdmin() {
        Flux<FileEvent> fileEvents = Flux.just(
                FileEvent.builder().fileId("1e").uploaderId("mark").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().fileId("7e").uploaderId("edward").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().fileId("3e").uploaderId("line").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().fileId("2e").uploaderId("purps02").actionType(ActionType.RESTRICT).build()
        );
        when(fileService.getAllFileEvents()).thenReturn(fileEvents);

        client.get()
                .uri("/api/files/admin/events")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(FileEvent[].class)
                    .isEqualTo(fileEvents.collectList().block().toArray(new FileEvent[0]));
        verify(fileService).getAllFileEvents();
    }

    @Test
    @WithMockUser
    public void getFileEventsAllAsUser_403() {
        client.get()
                .uri("/api/files/admin/events")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser
    public void getFileEventsForUser() {
        Flux<FileEvent> fileEvents = Flux.just(
                FileEvent.builder().fileId("1e").uploaderId("user").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().fileId("7e").uploaderId("user").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().fileId("3e").uploaderId("user").actionType(ActionType.RESTRICT).build(),
                FileEvent.builder().fileId("2e").uploaderId("user").actionType(ActionType.RESTRICT).build()
        );

        when(fileService.getFileEventsByUser("user")).thenReturn(fileEvents);

        client.get()
                .uri("/api/files/events")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(FileEvent.class)
                    .contains(fileEvents.collectList().block().toArray(new FileEvent[0]));
        verify(fileService).getFileEventsByUser("user");
    }

}
