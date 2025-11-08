package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.dto.request.EditSchoolCutOffPointRequest;
import com.sc2006.g5.edufinder.dto.response.SchoolsResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sc2006.g5.edufinder.service.SchoolService;

@RestController
@RequestMapping("api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<SchoolsResponse> getAllSchools() {
        SchoolsResponse schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{schoolId}")
    public ResponseEntity<?> editSchoolCutOffPoint(
        @NotNull @PathVariable Long schoolId,
        @Valid @RequestBody EditSchoolCutOffPointRequest request
    ){
        schoolService.editSchoolCutOffPoint(schoolId, request);
        return ResponseEntity.noContent().build();
    }
}
