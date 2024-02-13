package crazy_selling_store.service.impl;

import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.CommentEntity;
import crazy_selling_store.entity.UserEntity;
import crazy_selling_store.exceptions.EntityNotFoundException;
import crazy_selling_store.repository.AdRepository;
import crazy_selling_store.repository.CommentRepository;
import crazy_selling_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


// сервис для получения и обработки
//НЕОБХОДИМО ГРАМОТНО ВНЕДРИТЬ ЭТОТ КЛАСС В ЛОГИКУ СЕРВИСОВ
@Service
@RequiredArgsConstructor
public class EntityFromDBHandler {
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;

    public UserEntity getAndHandleUserFromDB(String username) throws EntityNotFoundException {
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    public AdEntity getAndHandleAdFromDB(Integer pk) throws EntityNotFoundException {
        return adRepository.findByPk(pk)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
    }

    public CommentEntity getAndHandleCommentFromDB(Integer pk) throws EntityNotFoundException {
        return commentRepository.findByPk(pk)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий не найден"));
    }
}
