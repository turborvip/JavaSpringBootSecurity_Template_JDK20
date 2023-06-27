package com.turborvip.security.domain.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginationResponseDTO<T> {
    private Integer status;
    private String message;
    private PaginateDTO<T> result;
}
