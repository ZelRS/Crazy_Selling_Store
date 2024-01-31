package crazy_selling_store.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "comments")
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private Long createdAt;
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
