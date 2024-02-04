package crazy_selling_store.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ads")
@NoArgsConstructor
@Data
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private Integer price;
    private String title;
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany (mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
