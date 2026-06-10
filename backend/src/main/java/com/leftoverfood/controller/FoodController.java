package com.leftoverfood.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.common.Result;
import com.leftoverfood.dto.LeftoverFoodQuery;
import com.leftoverfood.entity.FoodCategory;
import com.leftoverfood.entity.LeftoverFood;
import com.leftoverfood.service.FoodCategoryService;
import com.leftoverfood.service.LeftoverFoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "余餐管理", description = "余餐分类和登记管理接口")
@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodCategoryService foodCategoryService;

    @Autowired
    private LeftoverFoodService leftoverFoodService;

    @Operation(summary = "获取所有余餐分类")
    @GetMapping("/categories")
    public Result<List<FoodCategory>> getCategories() {
        return Result.success(foodCategoryService.listAll());
    }

    @Operation(summary = "新增余餐分类")
    @PostMapping("/category")
    public Result<FoodCategory> addCategory(@RequestBody FoodCategory category) {
        foodCategoryService.save(category);
        return Result.success(category);
    }

    @Operation(summary = "更新余餐分类")
    @PutMapping("/category")
    public Result<FoodCategory> updateCategory(@RequestBody FoodCategory category) {
        foodCategoryService.updateById(category);
        return Result.success(category);
    }

    @Operation(summary = "删除余餐分类")
    @DeleteMapping("/category/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        foodCategoryService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "分页查询余餐登记")
    @GetMapping("/page")
    public Result<PageResult<LeftoverFood>> getFoodPage(LeftoverFoodQuery query) {
        return Result.success(leftoverFoodService.pageQuery(query));
    }

    @Operation(summary = "获取余餐详情")
    @GetMapping("/{id}")
    public Result<LeftoverFood> getFoodById(@PathVariable Long id) {
        return Result.success(leftoverFoodService.getById(id));
    }

    @Operation(summary = "新增余餐登记")
    @PostMapping
    public Result<LeftoverFood> addFood(@RequestBody LeftoverFood food) {
        food.setFoodStatus(1);
        leftoverFoodService.save(food);
        return Result.success(food);
    }

    @Operation(summary = "更新余餐登记")
    @PutMapping
    public Result<LeftoverFood> updateFood(@RequestBody LeftoverFood food) {
        leftoverFoodService.updateById(food);
        return Result.success(food);
    }

    @Operation(summary = "更新余餐状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateFoodStatus(@PathVariable Long id, @RequestParam Integer status) {
        leftoverFoodService.updateStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "删除余餐登记")
    @DeleteMapping("/{id}")
    public Result<Void> deleteFood(@PathVariable Long id) {
        leftoverFoodService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "获取待领取余餐列表")
    @GetMapping("/available")
    public Result<List<LeftoverFood>> getAvailableFood() {
        LambdaQueryWrapper<LeftoverFood> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LeftoverFood::getFoodStatus, 1)
                .orderByDesc(LeftoverFood::getCreateTime);
        return Result.success(leftoverFoodService.list(wrapper));
    }
}
