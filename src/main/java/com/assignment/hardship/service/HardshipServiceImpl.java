package com.assignment.hardship.service;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.dto.HardshipSummaryResponse;
import com.assignment.hardship.entity.Customer;
import com.assignment.hardship.entity.Hardship;
import com.assignment.hardship.entity.HardshipHistory;
import com.assignment.hardship.mapper.HardshipMapper;
import com.assignment.hardship.repo.CustomerRepository;
import com.assignment.hardship.repo.HardshipHistoryRepository;
import com.assignment.hardship.repo.HardshipRepository;
import enums.Status;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class HardshipServiceImpl implements HardshipService {
    private final HardshipMapper hardshipMapper;
    private final CustomerRepository customerRepo;
    private final HardshipRepository hardshipRepo;
    private final HardshipHistoryRepository hardshipHistoryRepo;

    @Override
    public HardshipResponse registerHardship(HardshipRequest request) {
        // TODO: check duplicate request

        // build DTO and save to DB
        Customer savedCustomer = customerRepo.save(hardshipMapper.buildCustomer(request));

        Hardship hardship = hardshipMapper.buildHardship(request);
        hardship.setStatus(Status.PENDING);
        hardship.setCustomer(savedCustomer);
        hardship.setCreatedAt(LocalDateTime.now());
        hardship.setUpdatedAt(LocalDateTime.now());
        Hardship savedHardship = hardshipRepo.save(hardship);

        HardshipHistory hardshipHistory = hardshipMapper.buildHardshipHistory(request);
        hardshipHistory.setHardship(savedHardship);
        hardshipHistory.setUpdatedAt(LocalDateTime.now());
        hardshipHistoryRepo.save(hardshipHistory);

        // build response
        return hardshipMapper.buildResponse(savedHardship);
    }

    @Override
    public HardshipResponse updateHardship(HardshipRequest request) {
        return null;
    }

    @Override
    public List<HardshipSummaryResponse> getAllHardship() {
        return null;
    }
}
