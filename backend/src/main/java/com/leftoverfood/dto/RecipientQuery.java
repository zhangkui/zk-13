package com.leftoverfood.dto;

import com.leftoverfood.common.PageQuery;
import lombok.Data;

@Data
public class RecipientQuery extends PageQuery {
    private String recipientType;
    private Integer status;
}
