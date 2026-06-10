package com.leftoverfood.controller;

import com.leftoverfood.common.PageResult;
import com.leftoverfood.common.Result;
import com.leftoverfood.dto.RecipientQuery;
import com.leftoverfood.entity.Recipient;
import com.leftoverfood.service.RecipientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "领取人管理", description = "领取人信息管理接口")
@RestController
@RequestMapping("/recipient")
public class RecipientController {

    @Autowired
    private RecipientService recipientService;

    @Operation(summary = "分页查询领取人")
    @GetMapping("/page")
    public Result<PageResult<Recipient>> getRecipientPage(RecipientQuery query) {
        return Result.success(recipientService.pageQuery(query));
    }

    @Operation(summary = "获取领取人详情")
    @GetMapping("/{id}")
    public Result<Recipient> getRecipientById(@PathVariable Long id) {
        return Result.success(recipientService.getById(id));
    }

    @Operation(summary = "新增领取人")
    @PostMapping
    public Result<Recipient> addRecipient(@RequestBody Recipient recipient) {
        recipient.setRecipientStatus(1);
        recipient.setTotalClaimed(0);
        recipientService.save(recipient);
        return Result.success(recipient);
    }

    @Operation(summary = "更新领取人")
    @PutMapping
    public Result<Recipient> updateRecipient(@RequestBody Recipient recipient) {
        recipientService.updateById(recipient);
        return Result.success(recipient);
    }

    @Operation(summary = "更新领取人状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Recipient recipient = recipientService.getById(id);
        if (recipient == null) {
            throw new IllegalArgumentException("领取人不存在");
        }
        recipient.setRecipientStatus(status);
        recipientService.updateById(recipient);
        return Result.success();
    }

    @Operation(summary = "删除领取人")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRecipient(@PathVariable Long id) {
        recipientService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "获取正常状态的领取人列表")
    @GetMapping("/list")
    public Result<List<Recipient>> getRecipientList() {
        return Result.success(recipientService.lambdaQuery()
                .eq(Recipient::getRecipientStatus, 1)
                .orderByDesc(Recipient::getCreateTime)
                .list());
    }
}
