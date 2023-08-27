package ru.practicum.location.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private float lat;

    private float lon;
}
