package com.leftoverfood.controller;

import com.leftoverfood.common.Result;
import com.leftoverfood.entity.ClaimRecord;
import com.leftoverfood.entity.LeftoverFood;
import com.leftoverfood.entity.Recipient;
import com.leftoverfood.service.ClaimRecordService;
import com.leftoverfood.service.LeftoverFoodService;
import com.leftoverfood.service.RecipientService;
import com.leftoverfood.service.SignArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "仪表盘", description = "系统统计数据接口")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private LeftoverFoodService leftoverFoodService;

    @Autowired
    private RecipientService recipientService;

    @Autowired
    private ClaimRecordService claimRecordService;

    @Autowired
    private SignArchiveService signArchiveService;

    @Operation(summary = "获取统计数据")
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalFood = leftoverFoodService.count();
        long availableFood = leftoverFoodService.lambdaQuery().eq(LeftoverFood::getFoodStatus, 1).count();
        long claimedFood = leftoverFoodService.lambdaQuery().eq(LeftoverFood::getFoodStatus, 2).count();

        long totalRecipient = recipientService.count();
        long activeRecipient = recipientService.lambdaQuery().eq(Recipient::getRecipientStatus, 1).count();

        long totalClaim = claimRecordService.count();
        long inTransit = claimRecordService.lambdaQuery().eq(ClaimRecord::getUsageStatus, 1).count();
        long delivered = claimRecordService.lambdaQuery().eq(ClaimRecord::getUsageStatus, 2).count();
        long used = claimRecordService.lambdaQuery().eq(ClaimRecord::getUsageStatus, 3).count();

        long totalArchive = signArchiveService.count();

        stats.put("totalFood", totalFood);
        stats.put("availableFood", availableFood);
        stats.put("claimedFood", claimedFood);
        stats.put("totalRecipient", totalRecipient);
        stats.put("activeRecipient", activeRecipient);
        stats.put("totalClaim", totalClaim);
        stats.put("inTransit", inTransit);
        stats.put("delivered", delivered);
        stats.put("used", used);
        stats.put("totalArchive", totalArchive);

        return Result.success(stats);
    }
}
