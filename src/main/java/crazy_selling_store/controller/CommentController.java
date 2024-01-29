package crazy_selling_store.controller;

import crazy_selling_store.dto.comments.Comment;
import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {

    @GetMapping(value = "/{id}/comments", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение комментариев объявления")
    public ResponseEntity<Comments> getAdComments(@PathVariable("id") Integer id) {
        Comments stubObj = new Comments(); /*объект-заглушка*/
        int stub = 10; /*заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (stub < 10) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(stubObj);
    }

    @PostMapping(value = "/{id}/comments", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавление комментария к объявлению")
    public ResponseEntity<Comment> createAdComment(@PathVariable("id") Integer id,
                                                   @RequestBody(required = false) CreateOrUpdateComment text) {
        Comment stubObj = new Comment(); /*объект-заглушка*/
        int stub = 10; /*заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (stub < 10) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(stubObj);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удаление комментария")
    public ResponseEntity<Void> deleteAdComment(@PathVariable("adId") Integer adId,
                                           @PathVariable("commentId") Integer commentId) {
        int stub = 10; /*заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (stub < 10 && stub > 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (stub <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/{adId}/comments/{commentId}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Обновление комментария")
    public ResponseEntity<Comment> updateAdComment(@PathVariable("adId") Integer adId,
                                                   @PathVariable("commentId") Integer commentId,
                                                   @RequestBody(required = false) CreateOrUpdateComment createOrUpdateComment) {
        Comment stubObj = new Comment(); /*объект-заглушка*/
        int stub = 10; /*заглушка*/
        if (stub > 10) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (stub < 10 && stub > 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (stub <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(stubObj);
    }
}
