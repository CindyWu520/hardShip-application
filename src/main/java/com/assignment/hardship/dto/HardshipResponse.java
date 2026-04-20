package com.assignment.hardship.dto;

import enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class HardshipResponse {
    // hardship fields
    @Schema(description = "Hardship id (auto increment)", example = "1L")
    private Long hardshipId;
    @Schema(description = "Reason for hardship application (optional)", example = "Lost job")
    private String reason;
    @Schema(description = "Status for hardship application", example = "PENDING")
    private Status status;

    // customer fields
    @Schema(description = "Customer id (auto increment)", example = "1")
    private Long customerId;
    @Schema(description = "Customer name", example = "John Doe")
    private String name;
    @Schema(description = "Customer date of birth", example = "1990-01-22")
    private LocalDate dateOfBirth;
    @Schema(description = "Yearly income", example = "75000.00")
    private BigDecimal income;
    @Schema(description = "Yearly expense", example = "60000.00")
    private BigDecimal expenses;

    @Schema(description = "Timestamp for creating hardship application", example = "2026-04-19T00:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Timestamp for updating hardship application", example = "2026-04-19T00:00:00")
    private LocalDateTime updatedAt;
}
