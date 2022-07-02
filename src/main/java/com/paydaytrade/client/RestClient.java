package com.paydaytrade.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import static com.paydaytrade.util.StringUtil.isBlank;


@Service
public class RestClient {

    private final Logger logger;
    private final RestTemplate restTemplate;

    public RestClient(Logger logger, RestTemplate restTemplate) {
        this.logger = logger;
        this.restTemplate = restTemplate;
    }

    private static <T> T parse(String str, Class<T> responseClass) {
        try {
            if (isBlank(str) || responseClass == null) return null;
            return new ObjectMapper().readValue(str, responseClass);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseClass) {
        logger.info("url: {}", url);
        URI uri = UriComponentsBuilder.fromHttpUrl(url).encode(StandardCharsets.US_ASCII).build(true).toUri();
        return exec(() -> restTemplate.getForEntity(uri, responseClass), responseClass);
    }

    private <T> ResponseEntity<T> exec(Supplier<ResponseEntity> supplier, Class<T> response) {
        try {
            return supplier.get();
        } catch (HttpStatusCodeException httpException) {
            httpException.printStackTrace();
            logger.error("HttpClientException: {}", httpException.getLocalizedMessage());
            logger.error("HttpStatusCodeException: ", httpException);
            return ResponseEntity.status(httpException.getStatusCode()).body(parse(httpException.getResponseBodyAsString(), response));
        } catch (Exception exception) {
            logger.error("Exception: {}", exception.getLocalizedMessage());
            return ResponseEntity.status(500).build();
        }
    }

}
