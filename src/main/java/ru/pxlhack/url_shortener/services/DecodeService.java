package ru.pxlhack.url_shortener.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pxlhack.url_shortener.exception.TokenExpiredException;
import ru.pxlhack.url_shortener.models.URLMapping;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DecodeService {
    private final URLMappingRepository urlMappingRepository;

    public String getLongURL(String token) {
        if (token == null || token.isEmpty())
            throw new IllegalArgumentException("Invalid token: " + token);


        Optional<URLMapping> urlMappingOptional = urlMappingRepository.findByToken(token);

        URLMapping urlMapping = urlMappingOptional.orElseThrow(() -> new NoSuchElementException("Unknown token: " + token));


        if (urlMapping.isExpired())
            throw new TokenExpiredException("Token has expired");


        return urlMapping.getLongURL();
    }
}
