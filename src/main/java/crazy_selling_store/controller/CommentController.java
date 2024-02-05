package crazy_selling_store.controller;

import crazy_selling_store.dto.comments.Comment;
import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import crazy_selling_store.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {
    private final CommentServiceImpl commentService;

    @GetMapping(value = "/{id}/comments", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение комментариев объявления")
    public ResponseEntity<Comments> getAdComments(@PathVariable("id") Integer id, Authentication authentication) {
        Comments comments = commentService.getAdComments(id);
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (comments == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(comments);
    }

    @PostMapping(value = "/{id}/comments", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавление комментария к объявлению")
    public ResponseEntity<Comment> createAdComment(@PathVariable("id") Integer id,
                                                   @RequestBody CreateOrUpdateComment text,
                                                   Authentication authentication) {
        Comment comment = commentService.createAdComment(id, text, authentication);
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удаление комментария")
    public ResponseEntity<Void> deleteAdComment(@PathVariable("adId") Integer adId,
                                                @PathVariable("commentId") Integer commentId,
                                                Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!commentService.deleteAdComment(adId, commentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/{adId}/comments/{commentId}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление комментария")
    public ResponseEntity<Comment> updateAdComment(@PathVariable("adId") Integer adId,
                                                   @PathVariable("commentId") Integer commentId,
                                                   @RequestBody CreateOrUpdateComment text,
                                                   Authentication authentication) {
        Comment comment = commentService.updateAdComment(adId, commentId, text);
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(comment);
    }
}
