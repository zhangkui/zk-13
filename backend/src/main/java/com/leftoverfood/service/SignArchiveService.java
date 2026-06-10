package com.leftoverfood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leftoverfood.common.PageQuery;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.entity.SignArchive;

public interface SignArchiveService extends IService<SignArchive> {
    PageResult<SignArchive> pageQuery(PageQuery query);
}
