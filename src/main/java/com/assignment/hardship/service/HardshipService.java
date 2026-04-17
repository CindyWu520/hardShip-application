package com.assignment.hardship.service;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.dto.HardshipSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HardshipService {
    HardshipResponse registerHardship(HardshipRequest request);

    HardshipResponse updateHardship(Long id, HardshipRequest request);

    List<HardshipSummaryResponse> getAllHardship();
}
