package com.leftoverfood.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("claim_record")
public class ClaimRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long foodId;

    private Long recipientId;

    private BigDecimal claimQuantity;

    private String quantityUnit;

    private LocalDateTime claimTime;

    private String claimPurpose;

    private String destination;

    private String transportMethod;

    private LocalDateTime expectedArrivalTime;

    private LocalDateTime actualArrivalTime;

    private String usageFeedback;

    private Integer usageStatus;

    private String handler;

    private String remark;

    @TableField(exist = false)
    private String foodName;

    @TableField(exist = false)
    private String recipientName;

    @TableField(exist = false)
    private String recipientPhone;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
