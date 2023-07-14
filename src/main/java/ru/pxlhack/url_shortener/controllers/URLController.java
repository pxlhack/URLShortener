package ru.pxlhack.url_shortener.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.pxlhack.url_shortener.dto.URLDTO;
import ru.pxlhack.url_shortener.dto.URLResponse;
import ru.pxlhack.url_shortener.services.DecodeService;
import ru.pxlhack.url_shortener.services.EncodeService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/short")
public class URLController {

    private final EncodeService encodeService;
    private final DecodeService decodeService;


    @PostMapping
    public ResponseEntity<URLResponse> getShortURL(@RequestBody URLDTO urlRequest) {
        URLResponse response = encodeService.getShortURL(urlRequest);

        if (response.getStatus() == HttpStatus.CREATED.value()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.ok(response);

    }

    @GetMapping("{token}")
    public RedirectView getLongURL(@PathVariable String token) {

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(decodeService.getLongURL(token));

        return redirectView;
    }


}
