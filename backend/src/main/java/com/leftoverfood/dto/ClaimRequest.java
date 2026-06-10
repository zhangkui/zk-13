package com.leftoverfood.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClaimRequest {
    private Long foodId;
    private Long recipientId;
    private BigDecimal claimQuantity;
    private String quantityUnit;
    private String claimPurpose;
    private String destination;
    private String transportMethod;
    private LocalDateTime expectedArrivalTime;
    private String handler;
    private String remark;

    private String signType;
    private String signatureData;
    private String signatureImage;
    private String paperSignImage;
    private String signLocation;
    private Integer isRead;
    private Integer isUnderstood;
    private Integer isAccepted;
    private String witnessName;
    private String witnessPhone;
}
