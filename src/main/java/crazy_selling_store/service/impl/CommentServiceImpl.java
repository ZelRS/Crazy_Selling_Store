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

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public Comments getAdComments(Integer id) {
        AdEntity ad = adRepository.getAdByPk(id);
        if (ad == null) {
            return null;
        }
        List<Comment> comments = new ArrayList<>();
        for (CommentEntity comment : ad.getComments()) {
            comments.add(INSTANCE.toDTOComment(comment.getUser(), comment));
        }
        return new Comments(comments.size(), comments);
    }

    @Transactional
    @Override
    public Comment createAdComment(Integer id, CreateOrUpdateComment text,
                                   Authentication authentication) {
        AdEntity ad = adRepository.getAdByPk(id);
        if (ad == null) {
            return null;
        }
        CommentEntity comment = new CommentEntity();
        comment.setAd(ad);
        comment.setText(text.getText());
        LocalDateTime now = LocalDateTime.now();
        Long createdAt = now.toInstant(ZoneOffset.UTC).toEpochMilli();
        comment.setCreatedAt(createdAt);

        UserEntity userFromDB = null;
        try {
            userFromDB = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        comment.setUser(userFromDB);
        commentRepository.save(comment);
        return INSTANCE.toDTOComment(comment.getUser(), comment);
    }

    @Transactional
    @Override
    public boolean deleteAdComment(Integer adId, Integer commentId) {
        CommentEntity comment = commentRepository.getCommentByPk(commentId);
        AdEntity ad = adRepository.getAdByPk(adId);
        if (ad == null || comment == null) {
            return false;
        }
        if (!comment.getAd().equals(ad)) {
            return false;
        }
        commentRepository.delete(comment);
        return true;
    }

    @Transactional
    @Override
    public Comment updateAdComment(Integer adId, Integer commentId,
                                   CreateOrUpdateComment text) {
        CommentEntity comment = commentRepository.getCommentByPk(commentId);
        AdEntity ad = adRepository.getAdByPk(adId);
        if (ad == null || comment == null) {
            return null;
        }
        if (!comment.getAd().equals(ad)) {
            return null;
        }
        comment.setText(text.getText());
        commentRepository.save(comment);
        return INSTANCE.toDTOComment(comment.getUser(), comment);
    }
}
