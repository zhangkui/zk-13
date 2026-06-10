package com.leftoverfood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leftoverfood.entity.RiskNotice;
import com.leftoverfood.mapper.RiskNoticeMapper;
import com.leftoverfood.service.RiskNoticeService;
import org.springframework.stereotype.Service;

@Service
public class RiskNoticeServiceImpl extends ServiceImpl<RiskNoticeMapper, RiskNotice> implements RiskNoticeService {

    @Override
    public RiskNotice getCurrent() {
        LambdaQueryWrapper<RiskNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RiskNotice::getIsCurrent, 1)
                .orderByDesc(RiskNotice::getCreateTime)
                .last("LIMIT 1");
        return this.getOne(wrapper);
    }

    @Override
    public boolean setCurrent(Long id) {
        RiskNotice notice = this.getById(id);
        if (notice == null) {
            throw new IllegalArgumentException("风险告知书不存在");
        }
        LambdaUpdateWrapper<RiskNotice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(RiskNotice::getIsCurrent, 0);
        this.update(updateWrapper);
        notice.setIsCurrent(1);
        return this.updateById(notice);
    }
}
