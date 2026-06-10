package com.leftoverfood.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("food_category")
public class FoodCategory implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String categoryName;

    private String categoryCode;

    private String description;

    private String storageCondition;

    private Integer shelfLifeHours;

    private Integer sortOrder;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
