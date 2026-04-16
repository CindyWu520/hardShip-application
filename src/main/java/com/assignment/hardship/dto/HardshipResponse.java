package com.assignment.hardship.dto;

import enums.Status;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class HardshipResponse {
    // hardship fields
    private Long hardshipId;
    private String reason;
    private Status status;

    // customer fields
    private Long customerId;
    private String name;
    private LocalDate dateOfBirth;
    private BigDecimal income;
    private BigDecimal expenses;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
