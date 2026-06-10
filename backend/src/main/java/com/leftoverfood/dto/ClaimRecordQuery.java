package com.leftoverfood.dto;

import com.leftoverfood.common.PageQuery;
import lombok.Data;

@Data
public class ClaimRecordQuery extends PageQuery {
    private Integer status;
    private Long foodId;
    private Long recipientId;
}
