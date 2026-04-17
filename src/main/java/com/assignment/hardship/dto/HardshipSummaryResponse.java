package com.assignment.hardship.dto;

import enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class HardshipSummaryResponse {
    private Long hardshipId;
    private String name;
    private String reason;
    private Status status;
    private LocalDateTime createdAt;
}
