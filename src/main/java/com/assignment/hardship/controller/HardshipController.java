package com.assignment.hardship.controller;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.dto.HardshipSummaryResponse;
import com.assignment.hardship.exception.ErrorResponse;
import com.assignment.hardship.service.HardshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Hardship", description = "Hardship Application API")
public class HardshipController {
    private final HardshipService hardshipService;

    @Operation(summary = "Register a new hardship application")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Hardship register successfully",
                    content = @Content(schema = @Schema(implementation = HardshipResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Hardship already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public ResponseEntity<HardshipResponse> createHardship(
            @RequestBody @Valid HardshipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hardshipService.registerHardship(request));
    }

    @Operation(summary = "Update existing hardship application")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Hardship updated successfully",
                    content = @Content(schema = @Schema(implementation = HardshipResponse.class))),
            @ApiResponse(responseCode = "404", description = "Hardship not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<HardshipResponse> updateHardship(
            @PathVariable Long id, @RequestBody @Valid HardshipRequest request) {
        return ResponseEntity.ok(hardshipService.updateHardship(id, request));
    }

    @Operation(summary = "Get all hardships")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of hardships")
    })
    @GetMapping
    public ResponseEntity<List<HardshipSummaryResponse>> getAll() {
        return ResponseEntity.ok(hardshipService.getAllHardship());
    }
}
