package ru.pxlhack.url_shortener.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pxlhack.url_shortener.dto.URLDTO;
import ru.pxlhack.url_shortener.dto.URLResponse;
import ru.pxlhack.url_shortener.exception.InvalidURLException;
import ru.pxlhack.url_shortener.models.URLMapping;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;
import ru.pxlhack.url_shortener.util.TokenGenerator;
import ru.pxlhack.url_shortener.util.URLValidator;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class EncodeService {

    private final int lifetime;
    private final String baseUrl;
    private final URLMappingRepository urlMappingRepository;

    public EncodeService(@Value("${token.lifetime}") int lifetime,
                         @Value("${base_url}") String baseUrl,
                         URLMappingRepository urlMappingRepository) {
        this.lifetime = lifetime;
        this.baseUrl = baseUrl;
        this.urlMappingRepository = urlMappingRepository;
    }


    @Transactional
    public URLResponse getShortURL(URLDTO urlDto) {
        String longUrl = urlDto.getLongUrl();

        if (!URLValidator.isValidURL(longUrl)) throw new InvalidURLException("Invalid URL: " + longUrl);

        Optional<URLMapping> urlMappingOptional = urlMappingRepository.findByLongURL(longUrl);

        if (urlMappingOptional.isEmpty()) {
            URLMapping createdUrlMapping = createUrlMapping(longUrl);
            return createdURLMappingResponse(createdUrlMapping.getToken());
        }

        URLMapping urlMapping = urlMappingOptional.get();

        if (urlMapping.isExpired())
            return createdURLMappingResponse(updateToken(urlMapping));


        return getShortURLResponse(urlMapping.getToken());
    }

    private URLResponse getShortURLResponse(String token) {
        return createURLResponse(HttpStatus.OK, getShortURLFromToken(token));
    }

    private URLResponse createdURLMappingResponse(String token) {
        return createURLResponse(HttpStatus.CREATED, getShortURLFromToken(token));
    }


    @Transactional
    public URLMapping createUrlMapping(String longUrl) {
        URLMapping urlMapping = new URLMapping();
        urlMapping.setLongURL(longUrl);
        urlMapping.setToken(TokenGenerator.generateToken());
        urlMapping.setCreatedAt(Date.from(Instant.now()));
        urlMapping.setExpiredAt(Date.from(Instant.now().plus(Duration.ofMinutes(lifetime))));

        return urlMappingRepository.save(urlMapping);
    }

    private URLResponse createURLResponse(HttpStatus status, String url) {
        return URLResponse.builder()
                .status(status.value())
                .urlDto(new URLDTO(url))
                .build();
    }

    private String getShortURLFromToken(String token) {
        return baseUrl + token;
    }

    @Transactional
    public String updateToken(URLMapping urlMapping) {
        urlMapping.setToken(TokenGenerator.generateToken());
        urlMapping.setCreatedAt(Date.from(Instant.now()));
        urlMapping.setExpiredAt(Date.from(Instant.now().plus(Duration.ofMinutes(lifetime))));
        urlMappingRepository.save(urlMapping);

        return urlMapping.getToken();
    }

}
