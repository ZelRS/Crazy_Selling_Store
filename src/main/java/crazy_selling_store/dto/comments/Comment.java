package crazy_selling_store.dto.comments;

import lombok.Data;

/**
 * DTO комментария
 */
@Data
public class Comment {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Integer pk;
    private String text;
}
