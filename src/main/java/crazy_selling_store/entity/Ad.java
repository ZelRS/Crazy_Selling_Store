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
    private Integer price;
    private String title;
    private String description;
    private String email;
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
