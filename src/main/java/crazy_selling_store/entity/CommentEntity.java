package crazy_selling_store.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//сущность комментария для БД
@Entity(name = "comments")
@NoArgsConstructor
@Data
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private Long createdAt;
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private AdEntity ad;
}
