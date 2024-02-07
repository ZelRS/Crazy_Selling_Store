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
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Comments> getAdComments(@PathVariable("id") Integer id) {
        Comments comments = commentService.getAdComments(id);
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
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удаление комментария")
    @PreAuthorize(value = "hasRole('ADMIN') or @commentServiceImpl.isCommentAuthor(authentication.getName(), #commentId)")
    public ResponseEntity<Void> deleteAdComment(@PathVariable("adId") Integer adId,
                                                @PathVariable("commentId") Integer commentId) {
        commentService.deleteAdComment(adId, commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/{adId}/comments/{commentId}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление комментария")
    @PreAuthorize(value = "hasRole('ADMIN') or @commentServiceImpl.isCommentAuthor(authentication.getName(), #commentId)")
    public ResponseEntity<Comment> updateAdComment(@PathVariable("adId") Integer adId,
                                                   @PathVariable("commentId") Integer commentId,
                                                   @RequestBody CreateOrUpdateComment text) {
        Comment comment = commentService.updateAdComment(adId, commentId, text);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(comment);
    }
}
