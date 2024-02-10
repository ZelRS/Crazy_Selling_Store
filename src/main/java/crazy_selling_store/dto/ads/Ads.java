package crazy_selling_store.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//DTO списка объявлений
@Data
@AllArgsConstructor
public class Ads {
    private Integer count;
    private List<Ad> results;
}
