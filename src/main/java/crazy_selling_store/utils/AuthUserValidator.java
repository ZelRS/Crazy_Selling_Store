package crazy_selling_store.utils;

import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.CommentEntity;
import crazy_selling_store.exceptions.EntityNotFoundException;
import crazy_selling_store.repository.AdRepository;
import crazy_selling_store.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//класс предназначен для использования внутри аннотации PreAuthorize, для валидации залогиненного пользователя
@Service
@RequiredArgsConstructor
public class AuthUserValidator {
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;

    //валидация пользователя при работе с объявлениями
    public boolean isAdAuthor(String username, Integer id) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
        return ad.getUser().getEmail().equals(username);
    }

    //валидация пользователя при работе с комментариями
    public boolean isCommentAuthor(String username, Integer id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
        return comment.getUser().getEmail().equals(username);
    }
}
