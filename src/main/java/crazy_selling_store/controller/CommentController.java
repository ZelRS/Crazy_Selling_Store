package crazy_selling_store.controller;

import crazy_selling_store.dto.comments.Comment;
import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import crazy_selling_store.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
/**
 * Контроллер для работы с комментариями объявлений.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {
    private final CommentServiceImpl commentService;
    /**
     * Получает комментарии для указанного объявления.
     *
     * @param id Идентификатор объявления.
     * @return ответ с комментариями объявления.
     */
    @Operation(
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Comments.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content())})
    @GetMapping(value = "/{id}/comments", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Comments> getAdComments(@PathVariable("id") Integer id) {
        Comments comments = commentService.getAdComments(id);
        if (comments == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Комментарии  объявления успешно получены");
        return ResponseEntity.ok(comments);
    }
    /**
     * Создает комментарий для указанного объявления.
     *
     * @param id             Идентификатор объявления.
     * @param text           Текст комментария.
     * @param authentication Аутентификационные данные пользователя.
     * @return ответ с созданным комментарием.
     */
    @Operation(
            summary = "Добавление комментария к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Comment.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content())})
    @PostMapping(value = "/{id}/comments", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> createAdComment(@PathVariable("id") Integer id,
                                                   @RequestBody CreateOrUpdateComment text,
                                                   Authentication authentication) {
        log.info("Попытка добавления комментария к объявлению");
        Comment comment = commentService.createAdComment(id, text, authentication);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Пользователь " + authentication.getName() + " успешно добавил комментарий");
        return ResponseEntity.ok(comment);
    }
    /**
     * Удаляет комментарий для указанного объявления.
     *
     * @param adId           Идентификатор объявления.
     * @param commentId      Идентификатор комментария.
     * @return ответ с кодом состояния в зависимости от результата удаления.
     */
    @Operation(
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content())})
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize(value = "hasRole('ADMIN') or @authUserValidator.isCommentAuthor(authentication.getName(), #commentId)")
    public ResponseEntity<Void> deleteAdComment(@PathVariable("adId") Integer adId,
                                                @PathVariable("commentId") Integer commentId) {
        log.info("Попытка удаления комментария");
        if (!commentService.deleteAdComment(adId, commentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Комментарий успешно удален");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    /**
     * Обновляет комментарий для указанного объявления.
     *
     * @param adId                  Идентификатор объявления.
     * @param commentId             Идентификатор комментария.
     * @param text Данные для обновления комментария.
     * @return ответ с обновленным комментарием
     */
    @Operation(
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Comment.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content())})
    @PatchMapping(value = "/{adId}/comments/{commentId}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN') or @authUserValidator.isCommentAuthor(authentication.getName(), #commentId)")
    public ResponseEntity<Comment> updateAdComment(@PathVariable("adId") Integer adId,
                                                   @PathVariable("commentId") Integer commentId,
                                                   @RequestBody CreateOrUpdateComment text) {
        log.info("Попытка обновления комментария");
        Comment comment = commentService.updateAdComment(adId, commentId, text);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Комментарий успешно обновлен");
        return ResponseEntity.ok(comment);
    }
}
