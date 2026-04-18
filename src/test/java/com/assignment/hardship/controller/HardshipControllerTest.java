package com.assignment.hardship.controller;

import com.assignment.hardship.config.JpaAuditingConfig;
import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.dto.HardshipSummaryResponse;
import com.assignment.hardship.exception.ErrorCode;
import com.assignment.hardship.exception.HardshipException;
import com.assignment.hardship.service.HardshipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = HardshipController.class, excludeAutoConfiguration = JpaAuditingConfig.class)
public class HardshipControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private HardshipService hardshipService;

    private HardshipRequest request;
    private HardshipResponse response;
    private HardshipSummaryResponse summaryResponse;
    private HardshipRequest invalidRequest;

    private final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 4, 18, 0, 0);
    private final LocalDate DATE_OF_BIRTH = LocalDate.of(1995, 11, 22);
    private final BigDecimal INCOME = new BigDecimal("20000");
    private final BigDecimal EXPENSES = new BigDecimal("10000");
    private final String URI = "/api/v1/hardship";

    public HardshipControllerTest() {
    }

    @BeforeEach
    void setUp() {
        // Arrange
        request = HardshipRequest.builder()
                .name("cindy")
                .dateOfBirth(LocalDate.parse("1995-11-22"))
                .income(new BigDecimal("10000"))
                .expenses(new BigDecimal("5000"))
                .reason("Unemployed")
                .build();

        response = HardshipResponse.builder()
                .hardshipId(1L)
                .reason("Unemployed")
                .status(Status.PENDING)
                .customerId(1L)
                .name("cindy")
                .dateOfBirth(DATE_OF_BIRTH)
                .income(INCOME)
                .expenses(EXPENSES)
                .createdAt(FIXED_TIME)
                .updatedAt(FIXED_TIME)
                .build();

        summaryResponse = HardshipSummaryResponse.builder()
                .hardshipId(1L)
                .name("cindy")
                .reason("Unemployed")
                .status(Status.PENDING)
                .createdAt(FIXED_TIME)
                .build();

        invalidRequest = HardshipRequest.builder()
                .name("")
                .dateOfBirth(null)
                .income(null)
                .expenses(null)
                .build();
    }

    // happy path
    @Test
    void shouldReturn201WhenHardshipIsValid() throws Exception {
        // Arrange
        when(hardshipService.registerHardship(any())).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hardshipId").value(1L))
                .andExpect(jsonPath("$.reason").value("Unemployed"))
                .andExpect(jsonPath("$.status").value("PENDING")) // String
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.name").value("cindy"))
                .andExpect(jsonPath("$.dateOfBirth").value("1995-11-22")) // String
                .andExpect(jsonPath("$.income").value(INCOME))
                .andExpect(jsonPath("$.expenses").value(EXPENSES))
                .andExpect(jsonPath("$.createdAt").value("2026-04-18T00:00:00")) //String
                .andExpect(jsonPath("$.updatedAt").value("2026-04-18T00:00:00"));
    }

    @Test
    void shouldReturn200whenUpdateHardshipSuccessful() throws Exception {
        // Arrange
        when(hardshipService.updateHardship(any(), any())).thenReturn(response);

        mockMvc.perform(put(URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hardshipId").value(1L))
                .andExpect(jsonPath("$.reason").value("Unemployed"))
                .andExpect(jsonPath("$.status").value("PENDING")) // String
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.name").value("cindy"))
                .andExpect(jsonPath("$.dateOfBirth").value("1995-11-22")) // String
                .andExpect(jsonPath("$.income").value(INCOME))
                .andExpect(jsonPath("$.expenses").value(EXPENSES))
                .andExpect(jsonPath("$.createdAt").value("2026-04-18T00:00:00")) //String
                .andExpect(jsonPath("$.updatedAt").value("2026-04-18T00:00:00"));
    }

    @Test
    void shouldReturn200WhenGetAll() throws Exception {
        // Arrange
        when(hardshipService.getAllHardship()).thenReturn(List.of(summaryResponse));

        // Act + Assert
        mockMvc.perform(get(URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hardshipId").value(1L))
                .andExpect(jsonPath("$[0].name").value("cindy"))
                .andExpect(jsonPath("$[0].reason").value("Unemployed"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].createdAt").value("2026-04-18T00:00:00"));
    }

    // failure path
    @Test
    void shouldThrowWhenFieldsNull() throws Exception {
        // Act + Assert
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid Request"));

        verify(hardshipService, never()).registerHardship(any());
    }

    @Test
    void shouldThrowWhenHardshipAlreadyExists() throws Exception {
        // Arrange
        when(hardshipService.registerHardship(any()))
                .thenThrow(new HardshipException(ErrorCode.HARDSHIP_ALREADY_EXISTS));

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("HARDSHIP_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Hardship already exists"))
                .andExpect(jsonPath("$.status").value("409"));
    }

    @Test
    void shouldThrowWhenHardshipNotFound() throws Exception {
        // Arrange
        when(hardshipService.updateHardship(any(), any()))
                .thenThrow(new HardshipException(ErrorCode.HARDSHIP_NOT_FOUND));

        mockMvc.perform(put(URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("HARDSHIP_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Can't find target hardship record"))
                .andExpect(jsonPath("$.status").value("404"));
    }
}
