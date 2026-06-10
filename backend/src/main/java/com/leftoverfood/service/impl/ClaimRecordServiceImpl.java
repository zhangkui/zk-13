package com.leftoverfood.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.dto.ClaimRecordQuery;
import com.leftoverfood.dto.ClaimRequest;
import com.leftoverfood.entity.ClaimRecord;
import com.leftoverfood.entity.LeftoverFood;
import com.leftoverfood.entity.Recipient;
import com.leftoverfood.entity.RiskNotice;
import com.leftoverfood.entity.SignArchive;
import com.leftoverfood.mapper.ClaimRecordMapper;
import com.leftoverfood.service.ClaimRecordService;
import com.leftoverfood.service.LeftoverFoodService;
import com.leftoverfood.service.RecipientService;
import com.leftoverfood.service.RiskNoticeService;
import com.leftoverfood.service.SignArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ClaimRecordServiceImpl extends ServiceImpl<ClaimRecordMapper, ClaimRecord> implements ClaimRecordService {

    @Autowired
    private LeftoverFoodService leftoverFoodService;

    @Autowired
    private RecipientService recipientService;

    @Autowired
    private RiskNoticeService riskNoticeService;

    @Autowired
    private SignArchiveService signArchiveService;

    @Override
    public PageResult<ClaimRecord> pageQuery(ClaimRecordQuery query) {
        Page<ClaimRecord> page = new Page<>(query.getPageNum(), query.getPageSize());
        return PageResult.of(this.baseMapper.selectClaimRecordPage(page, query.getKeyword(), query.getStatus(), query.getFoodId(), query.getRecipientId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean claimFood(ClaimRequest request) {
        LeftoverFood food = leftoverFoodService.getById(request.getFoodId());
        if (food == null) {
            throw new IllegalArgumentException("余餐记录不存在");
        }
        if (food.getFoodStatus() != 1) {
            throw new IllegalArgumentException("该余餐不可领取");
        }
        if (food.getQuantity().compareTo(request.getClaimQuantity()) < 0) {
            throw new IllegalArgumentException("领取数量超过剩余数量");
        }

        Recipient recipient = recipientService.getById(request.getRecipientId());
        if (recipient == null) {
            throw new IllegalArgumentException("领取人不存在");
        }
        if (recipient.getRecipientStatus() != 1) {
            throw new IllegalArgumentException("领取人状态异常，无法领取");
        }

        RiskNotice currentNotice = riskNoticeService.getCurrent();
        if (currentNotice == null) {
            throw new IllegalArgumentException("请先设置风险告知书");
        }

        if (request.getIsAccepted() == null || request.getIsAccepted() != 1) {
            throw new IllegalArgumentException("必须接受风险告知条款");
        }

        ClaimRecord claimRecord = new ClaimRecord();
        claimRecord.setFoodId(request.getFoodId());
        claimRecord.setRecipientId(request.getRecipientId());
        claimRecord.setClaimQuantity(request.getClaimQuantity());
        claimRecord.setQuantityUnit(request.getQuantityUnit());
        claimRecord.setClaimTime(LocalDateTime.now());
        claimRecord.setClaimPurpose(request.getClaimPurpose());
        claimRecord.setDestination(request.getDestination());
        claimRecord.setTransportMethod(request.getTransportMethod());
        claimRecord.setExpectedArrivalTime(request.getExpectedArrivalTime());
        claimRecord.setUsageStatus(1);
        claimRecord.setHandler(request.getHandler());
        claimRecord.setRemark(request.getRemark());
        boolean claimResult = this.save(claimRecord);

        SignArchive signArchive = new SignArchive();
        signArchive.setClaimRecordId(claimRecord.getId());
        signArchive.setRecipientId(request.getRecipientId());
        signArchive.setNoticeId(currentNotice.getId());
        signArchive.setNoticeVersion(currentNotice.getNoticeVersion());
        signArchive.setNoticeContentSnapshot(currentNotice.getNoticeContent());
        signArchive.setSignType(request.getSignType());
        signArchive.setSignatureData(request.getSignatureData());
        signArchive.setSignatureImage(request.getSignatureImage());
        signArchive.setPaperSignImage(request.getPaperSignImage());
        signArchive.setSignTime(LocalDateTime.now());
        signArchive.setSignLocation(request.getSignLocation());
        signArchive.setIsRead(request.getIsRead());
        signArchive.setIsUnderstood(request.getIsUnderstood());
        signArchive.setIsAccepted(request.getIsAccepted());
        signArchive.setWitnessName(request.getWitnessName());
        signArchive.setWitnessPhone(request.getWitnessPhone());
        signArchive.setArchiveStatus(1);
        boolean signResult = signArchiveService.save(signArchive);

        BigDecimal remaining = food.getQuantity().subtract(request.getClaimQuantity());
        food.setQuantity(remaining);
        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            food.setFoodStatus(2);
        }
        boolean foodResult = leftoverFoodService.updateById(food);

        recipient.setTotalClaimed(recipient.getTotalClaimed() + 1);
        recipient.setLastClaimTime(LocalDateTime.now());
        boolean recipientResult = recipientService.updateById(recipient);

        return claimResult && signResult && foodResult && recipientResult;
    }

    @Override
    public boolean updateUsageStatus(Long id, Integer status, String feedback) {
        ClaimRecord record = this.getById(id);
        if (record == null) {
            throw new IllegalArgumentException("领取记录不存在");
        }
        record.setUsageStatus(status);
        if (feedback != null) {
            record.setUsageFeedback(feedback);
        }
        if (status == 2 && record.getActualArrivalTime() == null) {
            record.setActualArrivalTime(LocalDateTime.now());
        }
        return this.updateById(record);
    }
}
