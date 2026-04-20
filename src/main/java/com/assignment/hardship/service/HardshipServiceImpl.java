package com.assignment.hardship.service;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.dto.HardshipSummaryResponse;
import com.assignment.hardship.entity.Customer;
import com.assignment.hardship.entity.Hardship;
import com.assignment.hardship.entity.HardshipHistory;
import com.assignment.hardship.exception.ErrorCode;
import com.assignment.hardship.exception.HardshipException;
import com.assignment.hardship.mapper.HardshipMapper;
import com.assignment.hardship.repo.CustomerRepository;
import com.assignment.hardship.repo.HardshipHistoryRepository;
import com.assignment.hardship.repo.HardshipRepository;
import enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HardshipServiceImpl implements HardshipService {
    private final HardshipMapper hardshipMapper;
    private final CustomerRepository customerRepo;
    private final HardshipRepository hardshipRepo;
    private final HardshipHistoryRepository hardshipHistoryRepo;

    @Override
    @Transactional
    public HardshipResponse registerHardship(HardshipRequest request) {

        log.info("Registering hardship application for customer: {}", request.getName());

        // check duplicate request
        if (hardshipRepo.existsByCustomerName(request.getName())) {
            log.error("Hardship already exists. Customer name: {}", request.getName());
            throw new HardshipException(ErrorCode.HARDSHIP_ALREADY_EXISTS);
        }

        // build DTO and save to DB
        Customer savedCustomer = customerRepo.save(hardshipMapper.buildCustomer(request));
        log.info("Customer information saved to DB. CustomerId: {}", savedCustomer.getId());

        Hardship hardship = hardshipMapper.buildHardship(request);
        hardship.setStatus(Status.PENDING);
        hardship.setCustomer(savedCustomer);
        Hardship savedHardship = hardshipRepo.save(hardship);
        log.info("Hardship application saved to DB. HardshipId: {}", savedHardship.getId());

        HardshipHistory hardshipHistory = hardshipMapper.buildHardshipHistory(request);
        hardshipHistory.setHardship(savedHardship);
        hardshipHistoryRepo.save(hardshipHistory);
        log.info("Hardship history saved to DB. HardshipHistoryId: {}", hardshipHistory.getHardship());

        // build response
        return hardshipMapper.buildResponse(savedHardship);
    }

    @Override
    @Transactional
    public HardshipResponse updateHardship(Long id, HardshipRequest request) {

        log.info("Updating hardship application. HardshipId: {}", id);

        Hardship hardship = hardshipRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Hardship not found. HardshipId: {}", id);
                    return new HardshipException(ErrorCode.HARDSHIP_NOT_FOUND);
                });

        // always save super table then child table
        Customer customer = hardship.getCustomer();
        hardshipMapper.updateCustomer(request, customer);
        customerRepo.save(customer);
        log.info("Customer information updated successfully. CustomerId: {}", customer.getId());

        hardshipMapper.updateHardship(request, hardship);
        Hardship savedHardship = hardshipRepo.save(hardship);
        log.info("Hardship application updated successfully. HardshipId: {}", hardship.getId());

        HardshipHistory hardshipHistory = hardshipMapper.buildHardshipHistory(request);
        hardshipHistory.setHardship(savedHardship);
        hardshipHistoryRepo.save(hardshipHistory);
        log.info("Hardship History saved. HardshipHistoryId: {}", hardshipHistory.getId());

        return hardshipMapper.buildResponse(savedHardship);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HardshipSummaryResponse> getAllHardship() {

        log.info("Fetching all hardship applications");

        List<Hardship> hardships = hardshipRepo.findAll();
        log.info("Found {} hardship applications", hardships.size());
        return hardships.stream()
                .map(hardshipMapper::buildSummaryResponse)
                .toList();
    }
}
