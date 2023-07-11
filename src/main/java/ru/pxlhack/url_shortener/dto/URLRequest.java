package ru.pxlhack.url_shortener.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class URLRequest {
    @NotEmpty
    private String url;
}
