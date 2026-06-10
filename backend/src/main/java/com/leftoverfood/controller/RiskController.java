package com.leftoverfood.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leftoverfood.common.PageQuery;
import com.leftoverfood.common.PageResult;
import com.leftoverfood.common.Result;
import com.leftoverfood.entity.RiskNotice;
import com.leftoverfood.entity.SignArchive;
import com.leftoverfood.service.RiskNoticeService;
import com.leftoverfood.service.SignArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "风险告知与签收管理", description = "风险告知书和签收留档管理接口")
@RestController
@RequestMapping("/risk")
public class RiskController {

    @Autowired
    private RiskNoticeService riskNoticeService;

    @Autowired
    private SignArchiveService signArchiveService;

    @Operation(summary = "获取当前生效的风险告知书")
    @GetMapping("/notice/current")
    public Result<RiskNotice> getCurrentNotice() {
        return Result.success(riskNoticeService.getCurrent());
    }

    @Operation(summary = "获取风险告知书列表")
    @GetMapping("/notice/list")
    public Result<java.util.List<RiskNotice>> getNoticeList() {
        LambdaQueryWrapper<RiskNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(RiskNotice::getCreateTime);
        return Result.success(riskNoticeService.list(wrapper));
    }

    @Operation(summary = "新增风险告知书")
    @PostMapping("/notice")
    public Result<RiskNotice> addNotice(@RequestBody RiskNotice notice) {
        notice.setIsCurrent(0);
        riskNoticeService.save(notice);
        return Result.success(notice);
    }

    @Operation(summary = "更新风险告知书")
    @PutMapping("/notice")
    public Result<RiskNotice> updateNotice(@RequestBody RiskNotice notice) {
        riskNoticeService.updateById(notice);
        return Result.success(notice);
    }

    @Operation(summary = "设置为当前版本")
    @PutMapping("/notice/{id}/current")
    public Result<Void> setCurrentNotice(@PathVariable Long id) {
        riskNoticeService.setCurrent(id);
        return Result.success();
    }

    @Operation(summary = "删除风险告知书")
    @DeleteMapping("/notice/{id}")
    public Result<Void> deleteNotice(@PathVariable Long id) {
        riskNoticeService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "分页查询签收记录")
    @GetMapping("/archive/page")
    public Result<PageResult<SignArchive>> getArchivePage(PageQuery query) {
        return Result.success(signArchiveService.pageQuery(query));
    }

    @Operation(summary = "获取签收详情")
    @GetMapping("/archive/{id}")
    public Result<SignArchive> getArchiveById(@PathVariable Long id) {
        return Result.success(signArchiveService.getById(id));
    }

    @Operation(summary = "根据领取记录ID获取签收信息")
    @GetMapping("/archive/claim/{claimRecordId}")
    public Result<SignArchive> getArchiveByClaimId(@PathVariable Long claimRecordId) {
        LambdaQueryWrapper<SignArchive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SignArchive::getClaimRecordId, claimRecordId)
                .orderByDesc(SignArchive::getCreateTime)
                .last("LIMIT 1");
        return Result.success(signArchiveService.getOne(wrapper));
    }
}
