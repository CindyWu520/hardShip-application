package com.assignment.hardship.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "hardship_history")
@Builder
@Data
public class HardshipHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private BigDecimal income;
    private BigDecimal expenses;
    private String reason;
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "hardship_id")
    private Hardship hardship;
}
