package com.assignment.hardship.dto;

import enums.Status;

import java.time.LocalDateTime;

public class HardshipSummaryResponse {
    private Long hardshipId;
    private String name;
    private String reason;
    private Status status;
    private LocalDateTime createdAt;
}
