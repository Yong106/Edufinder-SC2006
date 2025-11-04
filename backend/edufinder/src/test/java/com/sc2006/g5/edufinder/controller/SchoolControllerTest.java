package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.config.SecurityConfig;
import com.sc2006.g5.edufinder.dto.response.SchoolResponse;
import com.sc2006.g5.edufinder.dto.response.SchoolsResponse;
import com.sc2006.g5.edufinder.security.AuthFilter;
import com.sc2006.g5.edufinder.service.SchoolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SchoolController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        AuthFilter.class,
        SecurityConfig.class
    })
})
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class SchoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SchoolService schoolService;

    private static final Long SCHOOL_ID_1 = 1L;
    private static final Long SCHOOL_ID_2 = 2L;

    @Nested
    @DisplayName("GET /api/schools")
    class GetAllSchoolsTest {

        @Test
        @DisplayName("should return 200 with schools response when request valid")
        void shouldReturn200WithValidResponseWhenRequestValid() throws Exception {

            SchoolResponse school1 = SchoolResponse.builder()
                .id(SCHOOL_ID_1)
                .build();

            SchoolResponse school2 = SchoolResponse.builder()
                .id(SCHOOL_ID_2)
                .build();

            SchoolsResponse response = SchoolsResponse.builder()
                .schools(List.of(school1, school2))
                .build();

            when(schoolService.getAllSchools())
                .thenReturn(response);

            mockMvc.perform(get("/api/schools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schools[0].id").value(SCHOOL_ID_1))
                .andExpect(jsonPath("$.schools[1].id").value(SCHOOL_ID_2));

            verify(schoolService, times(1)).getAllSchools();
        }
    }
}
