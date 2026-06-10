package com.leftoverfood.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("leftover_food")
public class LeftoverFood implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String foodName;

    private Long categoryId;

    private String banquetType;

    private LocalDate banquetDate;

    private String banquetLocation;

    private BigDecimal quantity;

    private String quantityUnit;

    private LocalDateTime produceTime;

    private LocalDateTime expiryTime;

    private String storageMethod;

    private String temperature;

    private Integer foodStatus;

    private String description;

    private String ingredients;

    private String allergenInfo;

    private String registrar;

    private String remark;

    @TableField(exist = false)
    private String categoryName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
