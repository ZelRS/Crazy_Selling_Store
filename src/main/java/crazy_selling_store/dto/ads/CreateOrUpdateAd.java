package crazy_selling_store.dto.ads;

import lombok.Data;

@Data
public class CreateOrUpdateAd {
    private String title;
    private Integer price;
    private String description;
}
