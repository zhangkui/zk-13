package com.leftoverfood.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("risk_notice")
public class RiskNotice implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String noticeTitle;

    private String noticeContent;

    private String noticeVersion;

    private Integer isCurrent;

    private LocalDate effectiveDate;

    private LocalDate expiryDate;

    private String riskItems;

    private String createBy;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
