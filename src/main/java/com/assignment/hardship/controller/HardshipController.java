package com.assignment.hardship.controller;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.dto.HardshipSummaryResponse;
import com.assignment.hardship.service.HardshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/hardship")
public class HardshipController {
    private final HardshipService hardshipService;

    @PostMapping
    public ResponseEntity<HardshipResponse> createHardship(
            @RequestBody @Valid HardshipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hardshipService.registerHardship(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HardshipResponse> updateHardship(
            @PathVariable Long id, @RequestBody @Valid HardshipRequest request) {
        return ResponseEntity.ok(hardshipService.updateHardship(id, request));
    }

    @GetMapping
    public ResponseEntity<List<HardshipSummaryResponse>> getAll() {
        return ResponseEntity.ok(hardshipService.getAllHardship());
    }
}
