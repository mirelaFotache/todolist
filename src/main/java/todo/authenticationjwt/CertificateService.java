package todo.authenticationjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import todo.service.exceptions.OAuth2Exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class CertificateService {

    private static final String CERTIFICATE_NAME = "src/main/resources/data/keystore.jks";
    private static final String URL_FOR_CERTIFICATE_RETRIEVAL = "http://localhost:8889/auth_asym_key/certificate";
    private static final String FILE_RETRIEVAL_FROM_URL_FAILED = "exception.file.retrieval.from.url.failed";
    private static final String FILE_HAS_BEEN_SAVED = "File has been saved";

    @Autowired
    private RestTemplate restTemplate;

    public void getCertificate() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ResponseEntity<Resource> exchange = restTemplate.exchange(URL_FOR_CERTIFICATE_RETRIEVAL, HttpMethod.GET, new HttpEntity<InputStream>(httpHeaders), Resource.class);
        try (InputStream inputStream = Objects.requireNonNull(exchange.getBody()).getInputStream();) {

            Future<String> s = saveFile(inputStream);
            System.out.println(s.get());
        } catch (Exception e) {
            throw new OAuth2Exception(FILE_RETRIEVAL_FROM_URL_FAILED);
        }
    }

    private Future<String> saveFile(InputStream inputStream) throws InterruptedException {
        CompletableFuture<String> completableFuture
                = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            File file = new File(".", CERTIFICATE_NAME);
            try (FileOutputStream outputStream = new FileOutputStream(file)) {

                int read;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }
            completableFuture.complete(FILE_HAS_BEEN_SAVED);
            return null;
        });

        return completableFuture;
    }
}
