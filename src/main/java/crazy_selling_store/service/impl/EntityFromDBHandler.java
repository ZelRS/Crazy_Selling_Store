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


/**
 * Сервис для получения и обработки сущностей из базы данных.
 */
@Service
@RequiredArgsConstructor
public class EntityFromDBHandler {
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;

    /**
     * Получить и обработать пользователя из базы данных.
     *
     * @param username username пользователя
     * @return сущность пользователя
     * @throws EntityNotFoundException если пользователь не найден
     */
    public UserEntity getAndHandleUserFromDB(String username) throws EntityNotFoundException {
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    /**
     * Получить и обработать объявление из базы данных.
     *
     * @param pk идентификатор объявления
     * @return сущность объявления
     * @throws EntityNotFoundException если объявление не найдено
     */
    public AdEntity getAndHandleAdFromDB(Integer pk) throws EntityNotFoundException {
        return adRepository.findByPk(pk)
                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
    }

    /**
     * Получить и обработать комментарий из базы данных.
     *
     * @param pk идентификатор комментария
     * @return сущность комментария
     * @throws EntityNotFoundException если комментарий не найден
     */
    public CommentEntity getAndHandleCommentFromDB(Integer pk) throws EntityNotFoundException {
        return commentRepository.findByPk(pk)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий не найден"));
    }
}
