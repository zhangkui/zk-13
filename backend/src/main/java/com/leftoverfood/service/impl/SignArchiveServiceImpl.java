package com.leftoverfood.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leftoverfood.common.PageQuery;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.entity.SignArchive;
import com.leftoverfood.mapper.SignArchiveMapper;
import com.leftoverfood.service.SignArchiveService;
import org.springframework.stereotype.Service;

@Service
public class SignArchiveServiceImpl extends ServiceImpl<SignArchiveMapper, SignArchive> implements SignArchiveService {

    @Override
    public PageResult<SignArchive> pageQuery(PageQuery query) {
        Page<SignArchive> page = new Page<>(query.getPageNum(), query.getPageSize());
        return PageResult.of(this.baseMapper.selectSignArchivePage(page, query.getKeyword(), null));
    }
}
