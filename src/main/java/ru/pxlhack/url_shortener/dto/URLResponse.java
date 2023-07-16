package ru.pxlhack.url_shortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class URLResponse {
    private int status;
    @JsonProperty(value = "data")
    private URLDTO urlDto;
}
