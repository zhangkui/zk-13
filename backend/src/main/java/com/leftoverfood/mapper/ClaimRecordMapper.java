package com.leftoverfood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leftoverfood.entity.ClaimRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClaimRecordMapper extends BaseMapper<ClaimRecord> {

    @Select("SELECT cr.*, lf.food_name, r.name AS recipient_name, r.phone AS recipient_phone " +
            "FROM claim_record cr " +
            "LEFT JOIN leftover_food lf ON cr.food_id = lf.id " +
            "LEFT JOIN recipient r ON cr.recipient_id = r.id " +
            "WHERE cr.is_deleted = 0 " +
            "AND (#{keyword} IS NULL OR lf.food_name LIKE CONCAT('%', #{keyword}, '%') OR r.name LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{status} IS NULL OR cr.usage_status = #{status}) " +
            "AND (#{foodId} IS NULL OR cr.food_id = #{foodId}) " +
            "AND (#{recipientId} IS NULL OR cr.recipient_id = #{recipientId}) " +
            "ORDER BY cr.claim_time DESC")
    IPage<ClaimRecord> selectClaimRecordPage(Page<ClaimRecord> page,
                                             @Param("keyword") String keyword,
                                             @Param("status") Integer status,
                                             @Param("foodId") Long foodId,
                                             @Param("recipientId") Long recipientId);
}
