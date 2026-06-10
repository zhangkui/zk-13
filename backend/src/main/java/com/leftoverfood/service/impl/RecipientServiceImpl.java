package com.leftoverfood.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.dto.RecipientQuery;
import com.leftoverfood.entity.Recipient;
import com.leftoverfood.mapper.RecipientMapper;
import com.leftoverfood.service.RecipientService;
import org.springframework.stereotype.Service;

@Service
public class RecipientServiceImpl extends ServiceImpl<RecipientMapper, Recipient> implements RecipientService {

    @Override
    public PageResult<Recipient> pageQuery(RecipientQuery query) {
        Page<Recipient> page = new Page<>(query.getPageNum(), query.getPageSize());
        return PageResult.of(this.baseMapper.selectRecipientPage(page, query.getKeyword(), query.getRecipientType(), query.getStatus()));
    }
}
