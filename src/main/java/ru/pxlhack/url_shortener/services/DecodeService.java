package ru.pxlhack.url_shortener.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pxlhack.url_shortener.models.URLMapping;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DecodeService {
    private final URLMappingRepository urlMappingRepository;

    public String getLongURL(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

        return urlMappingRepository.findByToken(token)
                .map(URLMapping::getLongURL)
                .orElseThrow(() -> new NoSuchElementException("Unknown token: " + token));
    }
}
