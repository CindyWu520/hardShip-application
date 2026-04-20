package com.assignment.hardship.dto;

import com.assignment.hardship.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class HardshipSummaryResponse {
    @Schema(description = "Hardship id (auto increment)", example = "1")
    private Long hardshipId;
    @Schema(description = "Customer name", example = "John Doe")
    private String name;
    @Schema(description = "Reason for hardship application (optional)", example = "Lost job")
    private String reason;
    @Schema(description = "Status for hardship application", example = "PENDING")
    private Status status;
    @Schema(description = "Timestamp for creating hardship application", example = "2026-04-19T00:00:00")
    private LocalDateTime createdAt;
}
