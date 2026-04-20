package com.assignment.hardship.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Customer full name", example = "John Doe")
    @NotBlank(message = "name can't be blank")
    private String name;

    @Schema(description = "Customer date of birth", example = "1990-01-22")
    @NotNull(message = "data of birth can't be blank")
    @Past
    private LocalDate dateOfBirth;

    @Schema(description = "Yearly income", example = "75000.00")
    @NotNull(message = "income can't be null")
    @DecimalMin(value = "0.01", message = "income must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "income must be up to 10 digits and 2 decimal places")
    private BigDecimal income;

    @Schema(description = "Yearly expense", example = "60000.00")
    @NotNull(message = "expenses can't be null")
    @DecimalMin(value = "0.01", message = "expenses must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "expenses must be up to 10 digits and 2 decimal places")
    private BigDecimal expenses;

    @Schema(description = "Reason for hardship application (optional)", example = "lost job")
    @Size(max = 522)
    private String reason; // optional
}
