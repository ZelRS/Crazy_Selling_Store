package crazy_selling_store.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Comments {
    private Integer count;
    private List<Comment> results;
}
