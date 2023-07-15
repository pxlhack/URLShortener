package ru.pxlhack.url_shortener.models;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class URLMappingTest {

    @Test
    public void isExpiredShouldReturnsTrueForExpiredDate() {
        URLMapping urlMapping = new URLMapping();
        urlMapping.setExpiredAt(getExpiredDate());

        assertTrue(urlMapping.isExpired());
    }

    @Test
    public void isExpiredShouldReturnsFalseForNonExpiredDate() {
        URLMapping urlMapping = new URLMapping();
        urlMapping.setExpiredAt(getFutureDate());

        assertFalse(urlMapping.isExpired());
    }


    private Date getExpiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        return calendar.getTime();
    }

    private Date getFutureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        return calendar.getTime();
    }
}