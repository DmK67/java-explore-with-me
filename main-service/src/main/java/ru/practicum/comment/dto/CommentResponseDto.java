package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private EventShortDto event;
    private UserShortDto author;
    private String text;
    private String state;
    private String createdOn;
    private String updatedOn;
    private String publishedOn;
}
