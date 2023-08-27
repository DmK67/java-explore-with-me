package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatsDto;
import ru.practicum.exceptions.ValidationRequestException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void addHit(HitDto hitDto) {
        statsRepository.save(HitMapper.toHit(hitDto));
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<Stats> stats;
        if (start.isAfter(end)) {
            throw new ValidationRequestException("Date start must be before date end");
        }
        if (unique) {
            if (uris == null) {
                stats = statsRepository.findAllUrisWithUniqueIp(start, end);
            } else {
                stats = statsRepository.findUrisWithUniqueIp(uris, start, end);
            }
        } else {
            if (uris == null) {
                stats = statsRepository.findAllUris(start, end);
            } else {
                stats = statsRepository.findUris(uris, start, end);
            }
        }
        return !stats.isEmpty() ? stats.stream().map(StatsMapper::toStatDto).collect(Collectors.toList())
                : Collections.emptyList();
    }

}
