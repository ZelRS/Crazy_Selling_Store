package crazy_selling_store.service.impl;

import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.entity.Ad;
import crazy_selling_store.entity.Comment;
import crazy_selling_store.entity.User;
import crazy_selling_store.repository.AdRepository;
import crazy_selling_store.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static crazy_selling_store.mapper.CommentMapper.INSTANCE;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final AdRepository adRepository;

    public Comments getAdComments(Integer id) {
        Ad ad = adRepository.getAdByPk(id);
        User user = ad.getUser();
        List<crazy_selling_store.dto.comments.Comment> comments = new ArrayList<>();
        for (Comment comment : ad.getComments()) {
            comments.add(INSTANCE.toDTOComment(user, comment));
        }
        return new Comments(comments.size(), comments);
    }
}
