package ru.pxlhack.url_shortener.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pxlhack.url_shortener.dto.URLRequest;
import ru.pxlhack.url_shortener.services.URLService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/short")
public class URLController {

    private final URLService urlService;

    @PostMapping
    public ResponseEntity<?> getShortURL(@RequestBody URLRequest urlRequest) {
        return urlService.createShortURL(urlRequest);
    }

}
