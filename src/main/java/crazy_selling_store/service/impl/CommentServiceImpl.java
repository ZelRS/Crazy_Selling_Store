package crazy_selling_store.service.impl;

import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import crazy_selling_store.entity.Ad;
import crazy_selling_store.entity.Comment;
import crazy_selling_store.entity.User;
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

    public Comments getAdComments(Integer id) {
        Ad ad = adRepository.getAdByPk(id);
        List<crazy_selling_store.dto.comments.Comment> comments = new ArrayList<>();
        for (Comment comment : ad.getComments()) {
            comments.add(INSTANCE.toDTOComment(comment.getUser(), comment));
        }
        return new Comments(comments.size(), comments);
    }

    @Transactional
    public crazy_selling_store.dto.comments.Comment createAdComment(Integer id, CreateOrUpdateComment text,
                                                                    Authentication authentication) {
        Ad ad = adRepository.getAdByPk(id);
        if (ad == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setAd(ad);
        comment.setText(text.getText());
        LocalDateTime now = LocalDateTime.now();
        Long createdAt = now.toInstant(ZoneOffset.UTC).toEpochMilli();
        comment.setCreatedAt(createdAt);

        User userFromDB = null;
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
    public boolean deleteAdComment(Integer adId, Integer commentId) {
        Comment comment = commentRepository.getCommentByPk(commentId);
        Ad ad = adRepository.getAdByPk(adId);
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
    public crazy_selling_store.dto.comments.Comment updateAdComment(Integer adId, Integer commentId,
                                                                    CreateOrUpdateComment text) {
        Comment comment = commentRepository.getCommentByPk(commentId);
        Ad ad = adRepository.getAdByPk(adId);
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
