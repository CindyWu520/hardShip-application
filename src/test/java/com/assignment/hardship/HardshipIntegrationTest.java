package com.assignment.hardship;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.repo.CustomerRepository;
import com.assignment.hardship.repo.HardshipHistoryRepository;
import com.assignment.hardship.repo.HardshipRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // load full Spring application context
@AutoConfigureMockMvc
@Transactional // roll back DB after every test
public class HardshipIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private HardshipRepository hardshipRepo;
    @Autowired
    private HardshipHistoryRepository hardshipHistoryRepo;


    private HardshipRequest request;
    private HardshipRequest updatedRequest;
    private final static String URI = "/api/v1/hardship";

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

        updatedRequest = HardshipRequest.builder()
                .name("cindy update")
                .dateOfBirth(LocalDate.parse("1988-11-22"))
                .income(new BigDecimal("20000"))
                .expenses(new BigDecimal("3000"))
                .build();
    }

    @Test
    void shouldRegisterHardshipAndSaveToDB() throws Exception {
        // Act
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.hardshipId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("cindy"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.income").value(10000));

        assertThat(customerRepo.findAll()).hasSize(1);
        assertThat(hardshipRepo.findAll()).hasSize(1);
        assertThat(hardshipHistoryRepo.findAll()).hasSize(1);
    }

    @Test
    void shouldUpdateHardshipAndSaveToDB() throws Exception {
        // Arrange - register first
        String response = mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();

        long hardshipId = objectMapper.readTree(response).get("hardshipId").asLong();

        // Act + Assert - update
        mockMvc.perform(put(URI + "/" + hardshipId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hardshipId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("cindy update"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.income").value(20000));

        assertThat(customerRepo.findAll()).hasSize(1);
        assertThat(hardshipRepo.findAll()).hasSize(1);
        assertThat(hardshipHistoryRepo.findAll()).hasSize(2);
    }

    @Test
    void shouldGetAllHardship() throws Exception {
        // Arrange - register first
        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        // Act & Assert
        mockMvc.perform(get(URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].hardshipId").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("cindy"));
    }

    // failure path
    @Test
    void shouldReturn409WhenDuplicateHardship() throws Exception {
        // Arrange - register first
        String response = mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("HARDSHIP_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Hardship already exists"))
                .andExpect(jsonPath("$.status").value("409"));
    }

    @Test
    void shouldReturn404WhenHardshipNotFound() throws Exception {
        // Act + Assert - update
        mockMvc.perform(put(URI + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("HARDSHIP_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Can't find target hardship record"))
                .andExpect(jsonPath("$.status").value("404"));
    }
}
