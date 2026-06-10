package com.leftoverfood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leftoverfood.entity.FoodCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodCategoryMapper extends BaseMapper<FoodCategory> {
}
