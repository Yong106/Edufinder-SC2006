package com.sc2006.g5.edufinder.unit.controller;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.controller.SchoolController;
import com.sc2006.g5.edufinder.dto.response.SchoolResponse;
import com.sc2006.g5.edufinder.dto.response.SchoolsResponse;
import com.sc2006.g5.edufinder.exception.school.CutOffPointException;
import com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException;
import com.sc2006.g5.edufinder.security.AuthFilter;
import com.sc2006.g5.edufinder.service.SchoolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SchoolController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class SchoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthFilter authFilter;

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

    @Nested
    @DisplayName("PUT /api/schools/{schoolId}")
    class EditSchoolCutOffPointTest {

        private static final Long EXISTED_SCHOOL_ID = 1L;
        private static final Long INVALID_SCHOOL_ID = 2L;

        private MockHttpServletRequestBuilder mockRawRequest(Long schoolId, String content) {
            return put("/api/schools/%d".formatted(schoolId), content)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(Long schoolId, int minCutOffPoint, int maxCutOffPoint) {
            return mockRawRequest(schoolId, """
                {"minCutOffPoint": %d, "maxCutOffPoint": %d}
            """.formatted(minCutOffPoint, maxCutOffPoint));
        }

        @Test
        @DisplayName("should return 204 when request valid")
        void shouldReturn204WhenRequestValid() throws Exception {
            int minCutOffPoint = 4;
            int maxCutOffPoint = 32;

            mockMvc.perform(mockRequest(EXISTED_SCHOOL_ID, minCutOffPoint, maxCutOffPoint))
                .andExpect(status().isNoContent());

            verify(schoolService, times(1)).editSchoolCutOffPoint(
                eq(EXISTED_SCHOOL_ID),
                argThat(request ->
                    request.getMinCutOffPoint() == minCutOffPoint &&
                    request.getMaxCutOffPoint() == maxCutOffPoint
                )
            );
        }

        @Test
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(mockRawRequest(EXISTED_SCHOOL_ID, "{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest(EXISTED_SCHOOL_ID, """
                {"minCutOffPoint": 4}
            """)).andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest(EXISTED_SCHOOL_ID, """
                {"maxCutOffPoint": 32}
            """)).andExpect(status().isBadRequest());

            verify(schoolService, never()).editSchoolCutOffPoint(any(), any());
        }

        @Test
        @DisplayName("should return 404 when school not found")
        void shouldReturn404WhenSchoolNotFound() throws Exception {
            int minCutOffPoint = 4;
            int maxCutOffPoint = 32;

            doThrow(new SchoolNotFoundException(INVALID_SCHOOL_ID)).when(schoolService)
                .editSchoolCutOffPoint(
                    eq(INVALID_SCHOOL_ID),
                    argThat(request ->
                        request.getMinCutOffPoint() == minCutOffPoint &&
                        request.getMaxCutOffPoint() == maxCutOffPoint
                    )
            );

            mockMvc.perform(mockRequest(INVALID_SCHOOL_ID, minCutOffPoint, maxCutOffPoint))
                .andExpect(status().isNotFound());

            verify(schoolService, times(1)).editSchoolCutOffPoint(any(), any());
        }

        @Test
        @DisplayName("should return 400 when min larger than man")
        void shouldReturn400WhenMinLargerThanMax() throws Exception {
            int minCutOffPoint = 4;
            int maxCutOffPoint = 32;

            doThrow(new CutOffPointException(maxCutOffPoint, minCutOffPoint)).when(schoolService)
                .editSchoolCutOffPoint(
                    eq(EXISTED_SCHOOL_ID),
                    argThat(request ->
                        request.getMinCutOffPoint() == maxCutOffPoint &&
                        request.getMaxCutOffPoint() == minCutOffPoint
                    )
                );

            mockMvc.perform(mockRequest(EXISTED_SCHOOL_ID, maxCutOffPoint, minCutOffPoint))
                    .andExpect(status().isBadRequest());

            verify(schoolService, times(1)).editSchoolCutOffPoint(any(), any());
        }

    }
}
