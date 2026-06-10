package com.leftoverfood.controller;

import com.leftoverfood.common.PageResult;
import com.leftoverfood.common.Result;
import com.leftoverfood.dto.ClaimRecordQuery;
import com.leftoverfood.dto.ClaimRequest;
import com.leftoverfood.entity.ClaimRecord;
import com.leftoverfood.service.ClaimRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "领取记录管理", description = "余餐领取和去向追踪接口")
@RestController
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimRecordService claimRecordService;

    @Operation(summary = "分页查询领取记录")
    @GetMapping("/page")
    public Result<PageResult<ClaimRecord>> getClaimPage(ClaimRecordQuery query) {
        return Result.success(claimRecordService.pageQuery(query));
    }

    @Operation(summary = "获取领取记录详情")
    @GetMapping("/{id}")
    public Result<ClaimRecord> getClaimById(@PathVariable Long id) {
        return Result.success(claimRecordService.getById(id));
    }

    @Operation(summary = "领取余餐（包含风险告知签收）")
    @PostMapping
    public Result<ClaimRecord> claimFood(@RequestBody ClaimRequest request) {
        claimRecordService.claimFood(request);
        return Result.success("领取成功", null);
    }

    @Operation(summary = "更新使用状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateUsageStatus(@PathVariable Long id,
                                          @RequestParam Integer status,
                                          @RequestParam(required = false) String feedback) {
        claimRecordService.updateUsageStatus(id, status, feedback);
        return Result.success();
    }

    @Operation(summary = "更新领取记录")
    @PutMapping
    public Result<ClaimRecord> updateClaim(@RequestBody ClaimRecord record) {
        claimRecordService.updateById(record);
        return Result.success(record);
    }

    @Operation(summary = "删除领取记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteClaim(@PathVariable Long id) {
        claimRecordService.removeById(id);
        return Result.success();
    }
}
