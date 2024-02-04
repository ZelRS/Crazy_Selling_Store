package crazy_selling_store.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Ads {
    private Integer count;
    private List<Ad> results;
}
