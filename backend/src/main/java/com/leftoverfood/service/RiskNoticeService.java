package com.leftoverfood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leftoverfood.entity.RiskNotice;

public interface RiskNoticeService extends IService<RiskNotice> {
    RiskNotice getCurrent();
    boolean setCurrent(Long id);
}
