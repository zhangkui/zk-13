package com.leftoverfood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leftoverfood.entity.FoodCategory;

import java.util.List;

public interface FoodCategoryService extends IService<FoodCategory> {
    List<FoodCategory> listAll();
}
