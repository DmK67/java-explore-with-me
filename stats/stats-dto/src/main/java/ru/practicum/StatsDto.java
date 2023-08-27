package ru.practicum;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StatsDto {
    private String app;
    private String uri;
    private int hits;

    @Override
    public String toString() {
        return "StatDto{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", hits=" + hits +
                '}';
    }
}
