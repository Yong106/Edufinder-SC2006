package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.model.school.ApiSchool;
import com.sc2006.g5.edufinder.repository.ApiSchoolRepository;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SchoolControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DbSchoolRepository dbSchoolRepository;

        @MockitoBean
    private ApiSchoolRepository apiSchoolRepository;

    @Test
    @DisplayName("GET /api/schools - success returns schools and persists missing ones")
    void getAllSchools_success_persistsAndReturns() throws Exception {
        // arrange: start with clean DB
        dbSchoolRepository.deleteAll();

        ApiSchool sch1 = ApiSchool.builder()
                .name("A School")
                .location("Central")
                .build();
        ApiSchool sch2 = ApiSchool.builder()
                .name("B School")
                .location("North")
                .build();
        ApiSchool sch3 = ApiSchool.builder()
                .name("C School")
                .location("")
                .build();
        ApiSchool sch4 = ApiSchool.builder()
                .name("")
                .location("North")
                .build();


        when(apiSchoolRepository.getApiSchools()).thenReturn(List.of(sch1, sch2, sch3, sch4));

        // act + assert
        mockMvc.perform(get("/api/schools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schools.length()").value(4))
                .andExpect(jsonPath("$.schools[0].name").value("A School"))
                .andExpect(jsonPath("$.schools[1].name").value("B School"))
                .andExpect(jsonPath("$.schools[2].name").value("C School"))
                .andExpect(jsonPath("$.schools[3].name").value(""));

        // verify persistence effect
        long count = dbSchoolRepository.count();
        org.junit.jupiter.api.Assertions.assertEquals(4L, count);
    }

    @Test
    @DisplayName("GET /api/schools - returns empty list when API has no schools")
    void getAllSchools_empty_whenApiReturnsNone() throws Exception {
        // arrange: clean DB and mock empty API response
        dbSchoolRepository.deleteAll();
        when(apiSchoolRepository.getApiSchools()).thenReturn(List.of());

        // act + assert: should return 200 with empty array
        mockMvc.perform(get("/api/schools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schools.length()").value(0));

        // verify: nothing persisted
        long count = dbSchoolRepository.count();
        org.junit.jupiter.api.Assertions.assertEquals(0L, count);
    }
}
