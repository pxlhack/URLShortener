package ru.pxlhack.url_shortener.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import ru.pxlhack.url_shortener.dto.URLDTO;
import ru.pxlhack.url_shortener.dto.URLResponse;
import ru.pxlhack.url_shortener.models.URLMapping;
import ru.pxlhack.url_shortener.repositories.URLMappingRepository;
import ru.pxlhack.url_shortener.util.TokenGenerator;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EncodeServiceTest {

    private EncodeService encodeService;

    private URLDTO urlDto;

    private final static String BASE_URL = "http://short/";

    private final static String LONG_URL = "http://example.com/long-url";

    private final static int TOKEN_LIFETIME_MINUTES = 60;

    @Mock
    private URLMappingRepository urlMappingRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        encodeService = new EncodeService(TOKEN_LIFETIME_MINUTES, BASE_URL, urlMappingRepository);
        urlDto = new URLDTO(LONG_URL);
    }

    @Test
    public void getShortURL_NewMappingCreated_ReturnsNewShortURL() {
        // Arrange
        when(urlMappingRepository.findByLongURL(LONG_URL)).thenReturn(Optional.empty());

        URLMapping savedUrlMapping = new URLMapping();
        savedUrlMapping.setToken("abc123");
        savedUrlMapping.setLongURL(LONG_URL);

        when(urlMappingRepository.save(any(URLMapping.class))).thenReturn(savedUrlMapping);

        // Act
        URLResponse response = encodeService.getShortURL(urlDto);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://short/abc123", response.getUrlDto().getLongUrl());

        verify(urlMappingRepository).findByLongURL(LONG_URL);
        verify(urlMappingRepository).save(any(URLMapping.class));
    }

    @Test
    public void getShortURL_ExistingMappingFoundAndNotExpired_ReturnsExistingShortURL() {
        // Arrange
        URLMapping existingUrlMapping = new URLMapping();
        existingUrlMapping.setToken("abc123");
        existingUrlMapping.setExpiredAt(getNotExpiredDate());

        when(urlMappingRepository.findByLongURL(LONG_URL)).thenReturn(Optional.of(existingUrlMapping));

        // Act
        URLResponse response = encodeService.getShortURL(urlDto);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("http://short/abc123", response.getUrlDto().getLongUrl());

        verify(urlMappingRepository).findByLongURL(LONG_URL);
        verify(urlMappingRepository, never()).save(any(URLMapping.class));
    }


    @Test
    public void getShortURL_ExistingMappingFoundAndExpired_ReturnsUpdatedShortURL() {
        // Arrange
        URLMapping expiredUrlMapping = new URLMapping();
        expiredUrlMapping.setToken("abc123");
        expiredUrlMapping.setExpiredAt(getExpiredDate());

        when(urlMappingRepository.findByLongURL(LONG_URL)).thenReturn(Optional.of(expiredUrlMapping));

        String updatedToken = "def456";

        try (MockedStatic<TokenGenerator> utilities = Mockito.mockStatic(TokenGenerator.class)) {
            utilities.when(TokenGenerator::generateToken).thenReturn(updatedToken);
            assertEquals(updatedToken, TokenGenerator.generateToken());

            URLMapping updatedUrlMapping = new URLMapping();
            updatedUrlMapping.setToken(updatedToken);
            when(urlMappingRepository.save(any(URLMapping.class))).thenReturn(updatedUrlMapping);

            // Act
            URLResponse response = encodeService.getShortURL(urlDto);

            // Assert
            assertEquals(HttpStatus.CREATED.value(), response.getStatus());
            assertEquals("http://short/def456", response.getUrlDto().getLongUrl());

            verify(urlMappingRepository).findByLongURL(LONG_URL);
            verify(urlMappingRepository).save(any(URLMapping.class));
        }
    }

    private Date getNotExpiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, TOKEN_LIFETIME_MINUTES);
        return calendar.getTime();
    }


    private Date getExpiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -TOKEN_LIFETIME_MINUTES);
        return calendar.getTime();
    }


}