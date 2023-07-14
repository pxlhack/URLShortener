package ru.pxlhack.url_shortener.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class URLResponse {
    private int status;
    private URLDTO urldto;
}
