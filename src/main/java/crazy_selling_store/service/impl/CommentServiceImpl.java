package crazy_selling_store.service.impl;

import crazy_selling_store.dto.comments.Comment;
import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.CommentEntity;
import crazy_selling_store.entity.UserEntity;
import crazy_selling_store.repository.AdRepository;
import crazy_selling_store.repository.CommentRepository;
import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static crazy_selling_store.mapper.CommentMapper.INSTANCE;

//сервисный класс для обработки комментариев
@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public Comments getAdComments(Integer id) {
        //получаем сущность объявления из БД по id
        AdEntity ad = adRepository.getAdByPk(id);
        if (ad == null) {
            //если объявления не нашлось возвращаем null из метода
            return null;
        }
        //формируем список DTO комментариев
        List<Comment> comments = new ArrayList<>();
        //проходим по списку сущностей комментариев и каждую сущность преобразовываем в DTO комментария
        for (CommentEntity comment : ad.getComments()) {
            comments.add(INSTANCE.toDTOComment(comment.getUser(), comment));
        }
        //формируем и возвращаем из метода DTO списка комментариев
        return new Comments(comments.size(), comments);
    }

    @Transactional
    @Override
    public Comment createAdComment(Integer id, CreateOrUpdateComment text,
                                   Authentication authentication) {
        //получаем сущность объявления из БД по id
        AdEntity ad = adRepository.getAdByPk(id);
        if (ad == null) {
            //если объявления не нашлось возвращаем null из метода
            return null;
        }
        //создаем новый объект комментария
        CommentEntity comment = new CommentEntity();
        //сеттим в комментарий объявлением и DTO CreateOrUpdateComment
        comment.setAd(ad);
        comment.setText(text.getText());
        //получаем актуальное время
        LocalDateTime now = LocalDateTime.now();
        //преобразовываем LocalDateTime в Long  и сеттим значение в комментарий
        Long createdAt = now.toInstant(ZoneOffset.UTC).toEpochMilli();
        comment.setCreatedAt(createdAt);
        //получаем зарегистрированного пользователя по имени аутентификации и сеттим его в комментарий
        UserEntity user = null;
        try {
            user = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        comment.setUser(user);
        //сохраняем сформированный комментарий в БД
        commentRepository.save(comment);
        //формируем и возвращаем в метод контроллера DTO комментария
        return INSTANCE.toDTOComment(comment.getUser(), comment);
    }

    @Transactional
    @Override
    public boolean deleteAdComment(Integer adId, Integer commentId) {
        //получаем комментария объявления из БД по id
        CommentEntity comment = commentRepository.getCommentByPk(commentId);
        //получаем сущность объявления из БД по id
        AdEntity ad = adRepository.getAdByPk(adId);
        //возвращаем из метода false, если комментарий или объявление не найдены
        if (ad == null || comment == null) {
            return false;
        }
        //возвращаем false, если объявление у комментария не соответствует объявлению, найденному по id
        if (!comment.getAd().equals(ad)) {
            return false;
        }
        //удаляем комментарий из БД
        commentRepository.delete(comment);
        //возвращаем из метода true
        return true;
    }

    @Transactional
    @Override
    public Comment updateAdComment(Integer adId, Integer commentId,
                                   CreateOrUpdateComment text) {
        //получаем комментария объявления из БД по id
        CommentEntity comment = commentRepository.getCommentByPk(commentId);
        //получаем сущность объявления из БД по id
        AdEntity ad = adRepository.getAdByPk(adId);
        //возвращаем из метода null, если комментарий или объявление не найдены
        if (ad == null || comment == null) {
            return null;
        }
        //возвращаем null, если объявление у комментария не соответствует объявлению, найденному по id
        if (!comment.getAd().equals(ad)) {
            return null;
        }
        //сеттим новый текст из DTO в комментарий и сохраняем его в БД
        comment.setText(text.getText());
        commentRepository.save(comment);
        //формируем и возвращаем в метод контроллера DTO комментария
        return INSTANCE.toDTOComment(comment.getUser(), comment);
    }
}
