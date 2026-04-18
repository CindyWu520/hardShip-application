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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HardshipServiceImpl implements HardshipService {
    private final HardshipMapper hardshipMapper;
    private final CustomerRepository customerRepo;
    private final HardshipRepository hardshipRepo;
    private final HardshipHistoryRepository hardshipHistoryRepo;

    @Override
    @Transactional
    public HardshipResponse registerHardship(HardshipRequest request) {
        // check duplicate request
        if (hardshipRepo.existsByCustomerName(request.getName())) {
            throw new HardshipException(ErrorCode.HARDSHIP_ALREADY_EXISTS);
        }

        // build DTO and save to DB
        Customer savedCustomer = customerRepo.save(hardshipMapper.buildCustomer(request));

        Hardship hardship = hardshipMapper.buildHardship(request);
        hardship.setStatus(Status.PENDING);
        hardship.setCustomer(savedCustomer);
        Hardship savedHardship = hardshipRepo.save(hardship);

        HardshipHistory hardshipHistory = hardshipMapper.buildHardshipHistory(request);
        hardshipHistory.setHardship(savedHardship);
        hardshipHistoryRepo.save(hardshipHistory);

        // build response
        return hardshipMapper.buildResponse(savedHardship);
    }

    @Override
    @Transactional
    public HardshipResponse updateHardship(Long id, HardshipRequest request) {
        Hardship hardship = hardshipRepo.findById(id)
                .orElseThrow(() -> new HardshipException(ErrorCode.HARDSHIP_NOT_FOUND));
        // always save super table then child table
        Customer customer = hardship.getCustomer();
        hardshipMapper.updateCustomer(request, customer);
        customerRepo.save(customer);

        hardshipMapper.updateHardship(request, hardship);
        Hardship savedHardship = hardshipRepo.save(hardship);

        HardshipHistory hardshipHistory = hardshipMapper.buildHardshipHistory(request);
        hardshipHistory.setHardship(savedHardship);
        hardshipHistoryRepo.save(hardshipHistory);

        return hardshipMapper.buildResponse(savedHardship);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HardshipSummaryResponse> getAllHardship() {
        List<Hardship> hardships = hardshipRepo.findAll();
        return hardships.stream()
                .map(hardshipMapper::buildSummaryResponse)
                .toList();
    }
}
