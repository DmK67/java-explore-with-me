package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "statistics")
public class Hit {

    /**
     * EndpointHit{
     * id	integer($int64)
     * readOnly: true
     * example: 1 Идентификатор записи
     * <p>
     * app	string
     * example: ewm-main-service Идентификатор сервиса для которого записывается информация
     * <p>
     * uri	string
     * example: /events/1 URI для которого был осуществлен запрос
     * <p>
     * ip	string
     * example: 192.163.0.1 IP-адрес пользователя, осуществившего запрос
     * <p>
     * timestamp	string
     * example: 2022-09-06 11:00:23 Дата и время, когда был совершен запрос к эндпоинту
     * (в формате "yyyy-MM-dd HH:mm:ss")}
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String app;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false, length = 40)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime timestamp;

}
