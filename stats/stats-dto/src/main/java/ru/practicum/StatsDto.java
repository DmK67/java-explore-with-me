package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
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
