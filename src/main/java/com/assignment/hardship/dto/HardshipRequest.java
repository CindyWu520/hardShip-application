package com.assignment.hardship.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class HardshipRequest {
    @NotBlank(message = "name can't be blank")
    private String name;

    @NotNull(message = "data of birth can't be blank")
    @Past
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @DecimalMin("0.01")
    @Digits(integer = 10, fraction = 2)
    @NotNull(message = "income can't be null")
    private BigDecimal income;

    @DecimalMin("0.01")
    @NotNull(message = "expenses can't be null")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal expenses;

    @Size(max = 522)
    private String reason; // optional
}
