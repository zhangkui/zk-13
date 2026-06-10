package com.leftoverfood.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("recipient")
public class Recipient implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String recipientType;

    private String name;

    private String idCard;

    private String orgCode;

    private String phone;

    private String email;

    private String address;

    private String emergencyContact;

    private String emergencyPhone;

    private String idCardFront;

    private String idCardBack;

    private String orgCertificate;

    private Integer recipientStatus;

    private Integer totalClaimed;

    private LocalDateTime lastClaimTime;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
