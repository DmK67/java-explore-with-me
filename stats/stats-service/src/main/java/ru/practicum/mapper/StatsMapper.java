package ru.practicum.mapper;

import ru.practicum.StatsDto;
import ru.practicum.model.Stats;

public class StatsMapper {

    public static StatsDto toStatDto(Stats stat) {
        return StatsDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits().intValue())
                .build();
    }
}
