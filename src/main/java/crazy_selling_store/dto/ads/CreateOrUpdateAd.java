package crazy_selling_store.dto.ads;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * DTO для работы с созданием или обновлением объявления
 */
@Data
public class CreateOrUpdateAd {
    @Size(min = 4, max = 32)
    private String title;
    @Size(max = 10000000)
    private Integer price;
    @Size(min = 8, max = 64)
    private String description;
}
