package com.leftoverfood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.dto.RecipientQuery;
import com.leftoverfood.entity.Recipient;

public interface RecipientService extends IService<Recipient> {
    PageResult<Recipient> pageQuery(RecipientQuery query);
}
