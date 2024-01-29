package crazy_selling_store.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ads")
@NoArgsConstructor
@Data
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User authorId;
    private String authorFirstName; // ?
    private String authorLastName; // ?
    private Integer price;
    private String title;
    private String description;
    private String email;
    @Lob
    private String image;

//    public Ad(String title, Integer price, String description, String image) {
//        this.title = title;
//        this.price = price;
//        this.description = description;
//        this.image = image;
//        this.authorFirstName = authorId.getFirstName();
//        this.authorLastName = authorId.getLastName();
//        this.email = authorId.getEmail();
//    }
}
