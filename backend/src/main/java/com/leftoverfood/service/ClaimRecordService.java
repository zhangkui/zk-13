package com.leftoverfood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.dto.ClaimRecordQuery;
import com.leftoverfood.dto.ClaimRequest;
import com.leftoverfood.entity.ClaimRecord;

public interface ClaimRecordService extends IService<ClaimRecord> {
    PageResult<ClaimRecord> pageQuery(ClaimRecordQuery query);
    boolean claimFood(ClaimRequest request);
    boolean updateUsageStatus(Long id, Integer status, String feedback);
}
