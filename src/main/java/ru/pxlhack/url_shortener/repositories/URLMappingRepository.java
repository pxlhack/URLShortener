package ru.pxlhack.url_shortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pxlhack.url_shortener.models.URLMapping;

public interface URLMappingRepository extends JpaRepository<URLMapping, Integer> {

}
