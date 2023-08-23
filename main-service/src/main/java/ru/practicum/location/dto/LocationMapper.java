package ru.practicum.location.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.location.Location;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}
