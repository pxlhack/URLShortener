package ru.pxlhack.url_shortener.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.pxlhack.url_shortener.exception.TokenExpiredException;
import ru.pxlhack.url_shortener.models.URLMapping;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void getLongURL_tokenIsNull_ThrowIllegalArgumentException() {
        // Arrange
        String token = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> decodeService.getLongURL(token));

        verify(urlMappingRepository, never()).findByToken(token);
    }

    @Test
    public void getLongURL_tokenIsEmpty_ThrowIllegalArgumentException() {
        // Arrange
        String token = "";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> decodeService.getLongURL(token));

        verify(urlMappingRepository, never()).findByToken(token);
    }

    @Test
    public void getLongURL_tokenIsNotFound_ThrowNoSuchElementException() {
        // Arrange
        String token = "anytoken";

        when(urlMappingRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> decodeService.getLongURL(token));
    }

    @Test
    public void getLongURL_tokenIsExpired_ThrowTokenExpiredException () {
        // Arrange
        String token = "anytoken";

        when(urlMappingRepository.findByToken(token)).thenReturn(Optional.of(expectedUrlMapping));
        when(expectedUrlMapping.isExpired()).thenReturn(true);

        // Act and Assert
        assertThrows(TokenExpiredException.class, () -> decodeService.getLongURL(token));
    }

}