package doctintuc.com.websitedoctintuc.domain.pagine;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginateDTO<T> {
    private List<T> pageData;
    private Integer currentPage;
    private Integer totalPage;
}
