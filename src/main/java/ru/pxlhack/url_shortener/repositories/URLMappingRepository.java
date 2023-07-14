package ru.pxlhack.url_shortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pxlhack.url_shortener.models.URLMapping;

import java.util.Optional;

public interface URLMappingRepository extends JpaRepository<URLMapping, Integer> {

    Optional<URLMapping> findByLongURL(String longURL);

    Optional<URLMapping> findByToken(String token);
}
