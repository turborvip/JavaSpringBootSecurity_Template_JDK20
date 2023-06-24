package com.turborvip.security.domain.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginateDTO<T> {
    private List<T> pageData;
    private Integer currentPage;
    private Integer totalPage;
}
