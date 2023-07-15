package ru.pxlhack.url_shortener.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pxlhack.url_shortener.dto.URLDTO;
import ru.pxlhack.url_shortener.dto.URLResponse;
import ru.pxlhack.url_shortener.models.URLMapping;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;
import ru.pxlhack.url_shortener.util.TokenGenerator;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EncodeService {

    @Value("${token.lifetime}")
    private int lifetime;

    @Value("${base_url}")
    private String baseUrl;


    private final URLMappingRepository urlMappingRepository;

    @Transactional
    public URLResponse getShortURL(URLDTO urlDto) {

        String url = urlDto.getUrl();

        Optional<URLMapping> urlMappingOptional = urlMappingRepository.findByLongURL(url);

        if (urlMappingOptional.isPresent()) {
            URLMapping urlMapping = urlMappingOptional.get();
            if (urlMapping.isExpired()) {
                String updatedToken = updateToken(urlMapping);

                return URLResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .urldto(new URLDTO(getShortURLFromToken(updatedToken)))
                        .build();

            }
            String shortURL = getShortURLFromToken(urlMapping.getToken());
            return URLResponse.builder()
                    .status(HttpStatus.OK.value())
                    .urldto(new URLDTO(shortURL))
                    .build();
        }

        return URLResponse.builder()
                .status(HttpStatus.CREATED.value())
                .urldto(new URLDTO(createShortURL(url)))
                .build();
    }

    @Transactional
    public String updateToken(URLMapping urlMapping) {
        urlMapping.setToken(TokenGenerator.generateToken());
        urlMapping.setCreatedAt(Date.from(Instant.now()));
        urlMapping.setExpiredAt(Date.from(Instant.now().plus(Duration.ofMinutes(lifetime))));
        urlMappingRepository.save(urlMapping);

        return urlMapping.getToken();
    }

    private String getShortURLFromToken(String token) {
        return baseUrl + token;
    }

    @Transactional
    public String createShortURL(String url) {
        URLMapping urlMapping = new URLMapping();
        urlMapping.setLongURL(url);
        urlMapping.setToken(TokenGenerator.generateToken());
        urlMapping.setCreatedAt(Date.from(Instant.now()));
        urlMapping.setExpiredAt(Date.from(Instant.now().plus(Duration.ofMinutes(lifetime))));

        urlMappingRepository.save(urlMapping);

        return getShortURLFromToken(urlMapping.getToken());
    }

}
