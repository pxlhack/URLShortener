package ru.pxlhack.url_shortener.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DecodeService {
    private final URLMappingRepository urlMappingRepository;

    public String getLongURL(String token) {
        //fixme
        return urlMappingRepository.findByToken(token).get().getLongURL();
    }
}
