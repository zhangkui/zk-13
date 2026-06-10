package com.leftoverfood.dto;

import com.leftoverfood.common.PageQuery;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeftoverFoodQuery extends PageQuery {
    private Integer status;
    private Long categoryId;
    private LocalDate startDate;
    private LocalDate endDate;
}
