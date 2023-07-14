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

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

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
    public URLResponse getShortURL(URLDTO URLDTO) {

        String url = URLDTO.getUrl();

        Optional<URLMapping> urlMappingOptional = urlMappingRepository.findByLongURL(url);

        if (urlMappingOptional.isPresent()) {
            String shortURL = getShortURLFromToken(urlMappingOptional.get().getToken());
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

    private String getShortURLFromToken(String token) {
        return baseUrl + token;
    }

    @Transactional
    public String createShortURL(String url) {


        URLMapping urlMapping = new URLMapping();
        urlMapping.setLongURL(url);
        urlMapping.setToken(getUniqueToken());
        urlMapping.setCreatedAt(new Date());
        urlMapping.setExpiredAt(Date.from(ZonedDateTime.now().plusMinutes(lifetime).toInstant()));

        urlMappingRepository.save(urlMapping);

        return getShortURLFromToken(urlMapping.getToken());
    }

    public String getUniqueToken() {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int LENGTH = 6;
        StringBuilder sb = new StringBuilder();

        do {
            Random random = new Random();
            for (int i = 0; i < LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }
        } while (urlMappingRepository.findByToken(sb.toString()).isPresent());

        return sb.toString();
    }

}
