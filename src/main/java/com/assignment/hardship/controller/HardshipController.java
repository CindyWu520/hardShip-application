package com.assignment.hardship.controller;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.service.HardshipService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("v1/hardship")
public class HardshipController {
    private final HardshipService hardshipService;

    @PostMapping
    public ResponseEntity<HardshipResponse> createHardship(@RequestBody
                                                           @Valid HardshipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hardshipService.registerHardship(request));
    }
}
