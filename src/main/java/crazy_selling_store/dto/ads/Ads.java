package crazy_selling_store.dto.ads;

import crazy_selling_store.dto.ads.Ad;
import lombok.Data;

import java.util.List;

@Data
public class Ads {
    private Integer count;
    private List<Ad> results;
}
