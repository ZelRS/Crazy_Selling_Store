package crazy_selling_store.dto.comments;

import lombok.Data;

import java.util.List;

@Data
public class Comments {
    private Integer count;
    private List<Comment> results;
}
