package com.assignment.hardship.service;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.dto.HardshipSummaryResponse;
import com.assignment.hardship.entity.Customer;
import com.assignment.hardship.entity.Hardship;
import com.assignment.hardship.entity.HardshipHistory;
import com.assignment.hardship.enums.Status;
import com.assignment.hardship.exception.HardshipException;
import com.assignment.hardship.mapper.HardshipMapper;
import com.assignment.hardship.repo.CustomerRepository;
import com.assignment.hardship.repo.HardshipHistoryRepository;
import com.assignment.hardship.repo.HardshipRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HardshipServiceTest {
    @Mock
    CustomerRepository customerRepo;
    @Mock
    HardshipRepository hardshipRepo;
    @Mock
    HardshipHistoryRepository hardshipHistoryRepo;
    @Mock
    HardshipMapper mapper;
    @Mock
    MeterRegistry meterRegistry;
    @Mock
    Counter counter;
    @Mock
    Timer timer;
    @InjectMocks
    HardshipServiceImpl hardshipService;

    private HardshipRequest request;
    private HardshipRequest updatedRequest;
    private Customer customer;
    private Hardship hardship;
    private HardshipHistory hardshipHistory;
    private HardshipResponse response;
    private HardshipResponse updatedResponse;
    private HardshipSummaryResponse summaryResponse;

    private final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 04, 17, 0, 0);

    @BeforeEach
    void setUp() {
        //Arrange
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
                .dateOfBirth(LocalDate.parse("1995-11-22"))
                .income(new BigDecimal("10000"))
                .expenses(new BigDecimal("5000"))
                .createdAt(FIXED_TIME)
                .updatedAt(FIXED_TIME)
                .build();

        updatedRequest = HardshipRequest.builder()
                .name("christy")
                .dateOfBirth(LocalDate.parse("1988-11-22"))
                .income(new BigDecimal("20000"))
                .expenses(new BigDecimal("3000"))
                .build();

        updatedResponse = HardshipResponse.builder()
                .hardshipId(1L)
                .status(Status.PENDING)
                .customerId(1L)
                .name("christy")
                .dateOfBirth(LocalDate.parse("1988-11-22"))
                .income(new BigDecimal("20000"))
                .expenses(new BigDecimal("3000"))
                .createdAt(FIXED_TIME)
                .updatedAt(FIXED_TIME)
                .build();

        customer = Customer.builder()
                .id(1L)
                .name("cindy")
                .dateOfBirth(LocalDate.parse("1995-11-22"))
                .income(new BigDecimal("10000"))
                .expenses(new BigDecimal("5000"))
                .build();

        hardship = Hardship.builder()
                .id(1L)
                .status(Status.PENDING)
                .reason("Unemployed")
                .createdAt(FIXED_TIME)
                .updatedAt(FIXED_TIME)
                .customer(customer)
                .build();

        hardshipHistory = HardshipHistory.builder()
                .id(1L)
                .name("cindy")
                .dateOfBirth(LocalDate.parse("1995-11-22"))
                .income(new BigDecimal("10000"))
                .expenses(new BigDecimal("5000"))
                .reason("Unemployed")
                .changedAt(FIXED_TIME)
                .hardship(hardship)
                .build();

        summaryResponse = HardshipSummaryResponse.builder()
                .hardshipId(1L)
                .name("cindy")
                .reason("Unemployed")
                .status(Status.PENDING)
                .createdAt(FIXED_TIME)
                .build();
    }

    // Happy path
    @Test
    void shouldRegisterHardshipSuccessfully() {
        // Arrange
        mockCount();
        mockTimer();
        when(hardshipRepo.existsByCustomerName(request.getName())).thenReturn(false);
        when(mapper.buildCustomer(any())).thenReturn(customer);
        when(customerRepo.save(any())).thenReturn(customer);
        when(mapper.buildHardship(any())).thenReturn(hardship);
        when(hardshipRepo.save(any())).thenReturn(hardship);
        when(mapper.buildHardshipHistory(any())).thenReturn(hardshipHistory);
        when(hardshipHistoryRepo.save(any())).thenReturn(hardshipHistory);
        when(mapper.buildResponse(any())).thenReturn(response);

        // Act
        HardshipResponse response = hardshipService.registerHardship(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getHardshipId());
        assertEquals("Unemployed", response.getReason());
        assertEquals(Status.PENDING, response.getStatus());

        assertEquals(1L, response.getCustomerId());
        assertEquals("cindy", response.getName());
        assertEquals(LocalDate.parse("1995-11-22"), response.getDateOfBirth());
        assertEquals(new BigDecimal("10000"), response.getIncome());
        assertEquals(new BigDecimal("5000"), response.getExpenses());
        assertEquals(FIXED_TIME, response.getCreatedAt());
        assertEquals(FIXED_TIME, response.getUpdatedAt());
        verify(customerRepo).save(customer);
        verify(hardshipRepo).save(hardship);
        verify(hardshipHistoryRepo).save(hardshipHistory);
    }

    @Test
    void shouldUpdateHardshipSuccessfully() {
        // Arrange
        mockCount();
        when(hardshipRepo.findById(any())).thenReturn(Optional.of(hardship));
        when(customerRepo.save(any())).thenReturn(customer);
        when(hardshipRepo.save(any())).thenReturn(hardship);
        when(mapper.buildHardshipHistory(any())).thenReturn(hardshipHistory);
        when(hardshipHistoryRepo.save(any())).thenReturn(hardshipHistory);
        when(mapper.buildResponse(any())).thenReturn(updatedResponse);

        // Act
        HardshipResponse response = hardshipService.updateHardship(1L, updatedRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getHardshipId());
        assertNull(response.getReason());
        assertEquals(Status.PENDING, response.getStatus());

        assertEquals(1L, response.getCustomerId());
        assertEquals("christy", response.getName());
        assertEquals(LocalDate.parse("1988-11-22"), response.getDateOfBirth());
        assertEquals(new BigDecimal("20000"), response.getIncome());
        assertEquals(new BigDecimal("3000"), response.getExpenses());
        assertEquals(FIXED_TIME, response.getCreatedAt());
        assertEquals(FIXED_TIME, response.getUpdatedAt());

        verify(mapper).updateCustomer(updatedRequest, customer);
        verify(mapper).updateHardship(updatedRequest, hardship);
        verify(customerRepo).save(customer);
        verify(hardshipRepo).save(hardship);
        verify(hardshipHistoryRepo).save(hardshipHistory);
    }

    @Test
    void shouldGetAllHardshipSuccessfully() {
        // Arrange
        when(hardshipRepo.findAll()).thenReturn(List.of(hardship));
        when(mapper.buildSummaryResponse(any())).thenReturn(summaryResponse);

        // Act
        List<HardshipSummaryResponse> responses = hardshipService.getAllHardship();

        // Assert
        assertNotNull(responses);
        assertThat(responses).containsExactlyInAnyOrder(summaryResponse);
    }

    // failure path
    @Test
    void shouldThrowWhenHardshipAlreadyExists() {
        // Arrange
        mockCount();
        when(hardshipRepo.existsByCustomerName(request.getName())).thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> hardshipService.registerHardship(request))
                .isInstanceOf(HardshipException.class)
                .hasMessage("Hardship already exists");
        verify(customerRepo, never()).save(any());
        verify(hardshipRepo, never()).save(any());
        verify(hardshipHistoryRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenHardshipNotFound() {
        // Arrange
        mockCount();
        when(hardshipRepo.findById(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> hardshipService.updateHardship(1L, request))
                .isInstanceOf(HardshipException.class)
                .hasMessage("Can't find target hardship record");
        verify(customerRepo, never()).save((any()));
        verify(hardshipRepo, never()).save((any()));
        verify(hardshipHistoryRepo, never()).save(any());
    }

    private void mockTimer() {
        when(meterRegistry.timer(any())).thenReturn(timer);
        when(timer.record(ArgumentMatchers.<Supplier<Object>>any())).thenAnswer(inv -> {
            Supplier<Object> supplier = inv.getArgument(0);
            return supplier.get();
        });
    }

    private void mockCount() {
        when(meterRegistry.counter(any())).thenReturn(counter);
        doNothing().when(counter).increment();
    }
}
