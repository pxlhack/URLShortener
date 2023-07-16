package ru.pxlhack.url_shortener.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.pxlhack.url_shortener.models.URLMapping;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DecodeServiceTest {

    private DecodeService decodeService;

    @Mock
    private URLMappingRepository urlMappingRepository;

    private URLMapping expectedUrlMapping;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        decodeService = new DecodeService(urlMappingRepository);
        expectedUrlMapping = mock(URLMapping.class);
    }


    @Test
    public void getLongURL_shouldReturnExpectedLongURL() {
        // Arrange
        String token = "abcdef23";
        String expectedLongUrl = "http://example.com/long-url";

        when(urlMappingRepository.findByToken(token)).thenReturn(Optional.of(expectedUrlMapping));
        when(expectedUrlMapping.getLongURL()).thenReturn(expectedLongUrl);
        when(expectedUrlMapping.isExpired()).thenReturn(false);

        // Act
        String actualLongUrl = decodeService.getLongURL(token);

        // Assert
        assertEquals(expectedLongUrl, actualLongUrl);

        verify(urlMappingRepository).findByToken(token);
    }
}