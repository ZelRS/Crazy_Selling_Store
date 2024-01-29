package crazy_selling_store.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User authorId;
    private String authorFirstName; // тут та же проблема ,что и в модели Ad. кажется, что поле лишнее... (смотреть модель Ad)
    private Long createdAt;
    private String text;
    @Lob
    private String authorImage; // тут та же проблема ,что и в модели Ad. кажется, что поле лишнее... (смотреть модель Ad)

//    public Comment(String text) {
//        this.authorImage = this.authorId.getImage();
//        this.authorFirstName = this.authorId.getFirstName();
//        this.text = text;
//    }
}
