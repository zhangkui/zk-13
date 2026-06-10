package com.leftoverfood.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sign_archive")
public class SignArchive implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long claimRecordId;

    private Long recipientId;

    private Long noticeId;

    private String noticeVersion;

    private String noticeContentSnapshot;

    private String signType;

    private String signatureData;

    private String signatureImage;

    private String paperSignImage;

    private LocalDateTime signTime;

    private String signIp;

    private String signLocation;

    private Integer isRead;

    private Integer isUnderstood;

    private Integer isAccepted;

    private String witnessName;

    private String witnessPhone;

    private Integer archiveStatus;

    private String remark;

    @TableField(exist = false)
    private String recipientName;

    @TableField(exist = false)
    private String foodName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
