package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.StatsDto;
import ru.practicum.model.Stats;

@UtilityClass
public class StatsMapper {

    public StatsDto toStatDto(Stats stat) {
        return StatsDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits().intValue())
                .build();
    }
}
