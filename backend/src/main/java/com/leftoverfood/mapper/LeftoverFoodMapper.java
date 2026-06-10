package com.leftoverfood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leftoverfood.entity.LeftoverFood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LeftoverFoodMapper extends BaseMapper<LeftoverFood> {

    @Select("SELECT lf.*, fc.category_name FROM leftover_food lf " +
            "LEFT JOIN food_category fc ON lf.category_id = fc.id " +
            "WHERE lf.is_deleted = 0 " +
            "AND (#{keyword} IS NULL OR lf.food_name LIKE CONCAT('%', #{keyword}, '%') OR lf.banquet_type LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{status} IS NULL OR lf.food_status = #{status}) " +
            "AND (#{categoryId} IS NULL OR lf.category_id = #{categoryId}) " +
            "ORDER BY lf.create_time DESC")
    IPage<LeftoverFood> selectLeftoverFoodPage(Page<LeftoverFood> page,
                                               @Param("keyword") String keyword,
                                               @Param("status") Integer status,
                                               @Param("categoryId") Long categoryId);
}
