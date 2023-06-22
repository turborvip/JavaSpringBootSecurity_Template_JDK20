package doctintuc.com.websitedoctintuc.domain.pagine;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginationResponseDTO<T> {
    private Integer status;
    private String message;
    private PaginateDTO<T> result;
}
