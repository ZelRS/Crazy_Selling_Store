package crazy_selling_store.entity;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private String text;

}
