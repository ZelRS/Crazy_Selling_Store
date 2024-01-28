package crazy_selling_store.dto;

import lombok.Data;

import java.util.List;

@Data
public class Ads {
    private Integer count;
    private List<Ad> results;
}
