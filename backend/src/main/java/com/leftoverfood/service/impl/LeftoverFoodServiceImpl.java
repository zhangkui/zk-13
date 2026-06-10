package com.leftoverfood.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.dto.LeftoverFoodQuery;
import com.leftoverfood.entity.LeftoverFood;
import com.leftoverfood.mapper.LeftoverFoodMapper;
import com.leftoverfood.service.LeftoverFoodService;
import org.springframework.stereotype.Service;

@Service
public class LeftoverFoodServiceImpl extends ServiceImpl<LeftoverFoodMapper, LeftoverFood> implements LeftoverFoodService {

    @Override
    public PageResult<LeftoverFood> pageQuery(LeftoverFoodQuery query) {
        Page<LeftoverFood> page = new Page<>(query.getPageNum(), query.getPageSize());
        return PageResult.of(this.baseMapper.selectLeftoverFoodPage(page, query.getKeyword(), query.getStatus(), query.getCategoryId()));
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        LeftoverFood food = this.getById(id);
        if (food == null) {
            throw new IllegalArgumentException("余餐记录不存在");
        }
        food.setFoodStatus(status);
        return this.updateById(food);
    }
}
