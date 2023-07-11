package ru.pxlhack.url_shortener.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pxlhack.url_shortener.dto.URLRequest;

@Service
public class URLService {
    public ResponseEntity<?> createNewShortURL(URLRequest urlRequest) {
        return null;
    }
}
