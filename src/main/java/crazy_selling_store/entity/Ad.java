package crazy_selling_store.entity;

import javax.persistence.*;

@Entity
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private Integer author;
    private String authorFirstName; // ?
    private String authorLastName; // ?
    private String image;
    private Integer price;
    private String title;
    private String description;
    private String email;
}
