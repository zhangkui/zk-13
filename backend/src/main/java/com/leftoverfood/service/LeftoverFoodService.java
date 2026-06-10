package com.leftoverfood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.dto.LeftoverFoodQuery;
import com.leftoverfood.entity.LeftoverFood;

public interface LeftoverFoodService extends IService<LeftoverFood> {
    PageResult<LeftoverFood> pageQuery(LeftoverFoodQuery query);
    boolean updateStatus(Long id, Integer status);
}
