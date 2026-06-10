package com.leftoverfood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leftoverfood.entity.Recipient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RecipientMapper extends BaseMapper<Recipient> {

    @Select("SELECT * FROM recipient WHERE is_deleted = 0 " +
            "AND (#{keyword} IS NULL OR name LIKE CONCAT('%', #{keyword}, '%') OR phone LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{type} IS NULL OR recipient_type = #{type}) " +
            "AND (#{status} IS NULL OR recipient_status = #{status}) " +
            "ORDER BY create_time DESC")
    IPage<Recipient> selectRecipientPage(Page<Recipient> page,
                                         @Param("keyword") String keyword,
                                         @Param("type") String type,
                                         @Param("status") Integer status);
}
