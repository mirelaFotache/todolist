package todo.authenticationjwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.exceptions.OAuth2Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class CertificateService {

    private static Logger log = LoggerFactory.getLogger(CertificateService.class);

    @Autowired
    private MessageSource messageSource;

    @Value("${certificate.nameAndPath}")
    private String nameAndPath;

    @Value("${jwt.base_url}")
    private String baseUrl;

    @Value("${certificate.url}")
    private String url;

    @Value("${certificate.bits}")
    private int bits;

    @Autowired
    private RestTemplate restTemplate;

    public void getCertificate() {
        boolean pageExists = true;
        final String url = baseUrl + this.url;
        try {
            restTemplate.headForHeaders(url);
        } catch (RestClientException e) {
            pageExists = false;
        }
        if (pageExists) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            ResponseEntity<Resource> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<InputStream>(httpHeaders), Resource.class);
            try (InputStream inputStream = Objects.requireNonNull(exchange.getBody()).getInputStream();) {

                Future<String> s = saveFile(inputStream);
                System.out.println(s.get());
            } catch (Exception e) {
                throw new OAuth2Exception(messageSource.getMessage("exception.file.retrieval.from.url.failed", null, LocaleContextHolder.getLocale()));
            }
        }
    }

    private Future<String> saveFile(InputStream inputStream) throws InterruptedException {
        CompletableFuture<String> completableFuture
                = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            File file = new File(new File("").getAbsolutePath(),nameAndPath);
            log.info(">>>>>>>>>>>>>>> file absolute path: <<< "+file.getAbsolutePath());

            try (FileOutputStream outputStream = new FileOutputStream(file)) {

                int read;
                byte[] bytes = new byte[bits];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }
            completableFuture.complete(messageSource.getMessage("notification.file.has.been.saved", null, LocaleContextHolder.getLocale()));
            return null;
        });

        return completableFuture;
    }
}
