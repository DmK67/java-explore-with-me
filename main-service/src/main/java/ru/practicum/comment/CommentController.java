package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.dto.NewCommentDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/user/{userId}/events/{eventId}/comment/")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@PathVariable @Positive Long userId,
                                            @PathVariable @Positive Long eventId,
                                            @RequestBody @Validated NewCommentDto newCommentDto) {
        return commentService.createComment(userId, eventId, newCommentDto);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentResponseDto> getEventComments(@PathVariable @Positive Long eventId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "10") @Positive int size) {
        return commentService.getEventComments(eventId, from, size);
    }

    @GetMapping("/comment/{commentId}")
    public CommentResponseDto getCommentById(@PathVariable @Positive Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @PatchMapping("/user/{userId}/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable @Positive Long userId,
                                            @PathVariable @Positive Long commentId,
                                            @RequestBody @Validated NewCommentDto newCommentDto) {
        return commentService.updateComment(userId, commentId, newCommentDto);
    }

    @DeleteMapping("/user/{userId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId,
                              @PathVariable @Positive Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @PatchMapping("/admin/comment/{commentId}")
    public CommentResponseDto updateCommentStatusByAdmin(@PathVariable @Positive Long commentId,
                                                         @RequestParam boolean isConfirm) {
        return commentService.updateCommentStatusByAdmin(commentId, isConfirm);
    }
}