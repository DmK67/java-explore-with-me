package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT new ru.practicum.model.Stats(s.app, s.uri, COUNT(s.uri)) FROM Hit s "
            + "WHERE s.uri IN (?1) AND s.timestamp BETWEEN ?2 AND ?3 GROUP BY s.app, s.uri ORDER BY COUNT(s.uri) DESC")
    List<Stats> findUris(String[] uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.model.Stats(s.app, s.uri, COUNT(DISTINCT s.ip)) FROM Hit s "
            + "WHERE s.uri IN (?1) AND s.timestamp BETWEEN ?2 AND ?3 GROUP BY s.app, s.uri, s.ip ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<Stats> findUrisWithUniqueIp(String[] uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.model.Stats(s.app, s.uri, COUNT(s.uri)) FROM Hit s "
            + "WHERE s.timestamp BETWEEN ?1 AND ?2 GROUP BY s.app, s.uri ORDER BY COUNT(s.uri) DESC")
    List<Stats> findAllUris(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.model.Stats(s.app, s.uri, COUNT(DISTINCT s.ip)) FROM Hit s "
            + "WHERE s.timestamp BETWEEN ?1 AND ?2 GROUP BY s.app, s.uri, s.ip ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<Stats> findAllUrisWithUniqueIp(LocalDateTime start, LocalDateTime end);
}
