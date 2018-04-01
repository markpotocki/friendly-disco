package dev.homely.filemanager.storage;

import dev.homely.filemanager.upload.storage.BasicStorageService;
import dev.homely.filemanager.upload.storage.StorageException;
import dev.homely.filemanager.upload.storage.StorageProperties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import reactor.core.publisher.Mono;

import java.util.Random;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;


public class BasicStorageServiceTest {

    private StorageProperties properties = new StorageProperties();
    private BasicStorageService storageService;

    @Before
    public void init() {
        properties.setLocation("target/test/" + Math.abs(new Random().nextLong()));
        this.storageService = new BasicStorageService(properties);
        storageService.init();
    }

    @Test
    public void saveAndLoad() {
        MockMultipartFile file = new MockMultipartFile("foo", "foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());
        storageService.store(file);
        assertNotNull(storageService.load("foo.txt"));
    }

    @Test
    public void fluxSaveAndLoad() {
        byte[] fileContent = "Hello World".getBytes();
        MockPart filePart = new MockPart("file", "Hello", fileContent);
        Mono<MockPart> monoPart = Mono.just(filePart);

        //storageService.store();
        assertNotNull(storageService.load("Hello"));
    }

    @Test
    public void loadNonExistentFile() {
        assertNull(storageService.loadAsResource("noexist.file").block());
    }

    @Test(expected = StorageException.class)
    public void saveInvalidBackOperator() {
        MockMultipartFile file = new MockMultipartFile("foo", "../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());
        storageService.store(file);
    }

    @Test
    public void saveValidFilename() {
        MockMultipartFile file = new MockMultipartFile("foo", "bar/../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes());
        storageService.store(file);
    }
}
