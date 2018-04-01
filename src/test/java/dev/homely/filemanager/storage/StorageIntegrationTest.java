package dev.homely.filemanager.storage;


import dev.homely.filemanager.upload.UploadController;
import dev.homely.filemanager.upload.storage.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
public class StorageIntegrationTest {

    private WebTestClient webClient;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setup() {
        webClient = WebTestClient
                .bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    @Test
    @WithMockUser
    public void sendValidFileSaveCorrectly() {
        MockMultipartFile file = new MockMultipartFile("foo", "foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file);

        webClient.mutateWith(csrf()).post()
                .uri("/api/file")
                .syncBody(builder.build())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
