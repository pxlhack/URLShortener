package ru.pxlhack.url_shortener.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pxlhack.url_shortener.dto.URLDTO;
import ru.pxlhack.url_shortener.dto.URLResponse;
import ru.pxlhack.url_shortener.services.URLService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/short")
public class URLController {

    private final URLService urlService;

    @PostMapping
    public ResponseEntity<URLResponse> getShortURL(@RequestBody URLDTO urlRequest) {
        URLResponse response = urlService.getShortURL(urlRequest);

        if (response.getStatus() == HttpStatus.CREATED.value()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.ok(response);

    }

}
