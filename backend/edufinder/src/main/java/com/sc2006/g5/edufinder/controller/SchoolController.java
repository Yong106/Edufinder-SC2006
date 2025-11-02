package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.dto.response.SchoolsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc2006.g5.edufinder.service.SchoolService;

@RestController
@RequestMapping("api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public SchoolsResponse getAllSchools() {
        return schoolService.getAllSchools();
    }
}
