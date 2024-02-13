package crazy_selling_store.dto.ads;

import lombok.Data;

//DTO объявления
@Data
public class Ad {
    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;
}
