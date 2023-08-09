package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    /** ViewStats{
        app	string
        example: ewm-main-service Название сервиса
        uri	string example: /events/1 URI сервиса
        hits	integer($int64) example: 6 Количество просмотров} */

    private String app;
    private String uri;
    private Long hits;

//    public ViewStats(String app, String uri, Long hits) {
//        this.app = app;
//        this.uri = uri;
//        this.hits = hits;
//    }
}
