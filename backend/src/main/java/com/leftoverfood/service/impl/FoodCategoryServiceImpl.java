package com.leftoverfood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leftoverfood.entity.FoodCategory;
import com.leftoverfood.mapper.FoodCategoryMapper;
import com.leftoverfood.service.FoodCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodCategoryServiceImpl extends ServiceImpl<FoodCategoryMapper, FoodCategory> implements FoodCategoryService {

    @Override
    public List<FoodCategory> listAll() {
        LambdaQueryWrapper<FoodCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodCategory::getStatus, 1)
                .orderByAsc(FoodCategory::getSortOrder)
                .orderByDesc(FoodCategory::getCreateTime);
        return this.list(wrapper);
    }
}
