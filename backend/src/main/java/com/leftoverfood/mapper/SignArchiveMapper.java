package com.leftoverfood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leftoverfood.entity.SignArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SignArchiveMapper extends BaseMapper<SignArchive> {

    @Select("SELECT sa.*, r.name AS recipient_name, lf.food_name " +
            "FROM sign_archive sa " +
            "LEFT JOIN recipient r ON sa.recipient_id = r.id " +
            "LEFT JOIN claim_record cr ON sa.claim_record_id = cr.id " +
            "LEFT JOIN leftover_food lf ON cr.food_id = lf.id " +
            "WHERE sa.is_deleted = 0 " +
            "AND (#{keyword} IS NULL OR r.name LIKE CONCAT('%', #{keyword}, '%') OR lf.food_name LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{status} IS NULL OR sa.archive_status = #{status}) " +
            "ORDER BY sa.sign_time DESC")
    IPage<SignArchive> selectSignArchivePage(Page<SignArchive> page,
                                             @Param("keyword") String keyword,
                                             @Param("status") Integer status);
}
