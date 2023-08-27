package ru.practicum.comment.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comment.Comment;

import java.time.format.DateTimeFormatter;

import static ru.practicum.event.dto.EventMapper.toEventShortDto;
import static ru.practicum.user.dto.UserMapper.toUserShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .build();
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .event(toEventShortDto(comment.getEvent()))
                .author(toUserShortDto(comment.getAuthor()))
                .text(comment.getText())
                .state(comment.getState().toString())
                .createdOn(comment.getCreatedOn().format(formatter))
                .updatedOn(comment.getUpdatedOn() != null ? comment.getUpdatedOn().format(formatter) : null)
                .publishedOn(comment.getPublishedOn() != null ? comment.getPublishedOn().format(formatter) : null)
                .build();
    }
}
