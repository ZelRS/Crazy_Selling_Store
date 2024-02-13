package crazy_selling_store.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

//сущность пользователя для БД
@Entity(name = "ads")
@NoArgsConstructor
@Data
public class AdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private Integer price;
    private String title;
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany (mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;
}
