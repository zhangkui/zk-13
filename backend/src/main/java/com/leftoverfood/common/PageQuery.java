package com.leftoverfood.common;

import lombok.Data;

@Data
public class PageQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private String sortField;
    private String sortOrder;
}
