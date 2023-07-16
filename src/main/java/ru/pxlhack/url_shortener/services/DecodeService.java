package ru.pxlhack.url_shortener.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pxlhack.url_shortener.exception.TokenExpiredException;
import ru.pxlhack.url_shortener.models.URLMapping;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DecodeService {
    private final URLMappingRepository urlMappingRepository;

    /**
     * Retrieves the long URL associated with the given token.
     *
     * @param token the token to decode
     * @return the long URL associated with the token
     * @throws IllegalArgumentException if the token is null or empty
     * @throws NoSuchElementException   if the token is not found
     * @throws TokenExpiredException    if the token has expired
     */
    public String getLongURL(String token) {
        if (token == null || token.isEmpty())
            throw new IllegalArgumentException("Invalid token: " + token);

        URLMapping urlMapping = urlMappingRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Unknown token: " + token));

        if (urlMapping.isExpired())
            throw new TokenExpiredException("Token has expired");

        return urlMapping.getLongURL();
    }
}
