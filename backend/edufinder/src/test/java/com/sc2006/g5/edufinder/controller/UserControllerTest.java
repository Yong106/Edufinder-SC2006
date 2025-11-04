package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.config.SecurityConfig;
import com.sc2006.g5.edufinder.dto.response.SavedSchoolResponse;
import com.sc2006.g5.edufinder.dto.response.UserResponse;
import com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException;
import com.sc2006.g5.edufinder.exception.user.UserAlreadySaveSchoolException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.exception.user.UserNotSaveSchoolException;
import com.sc2006.g5.edufinder.security.AuthFilter;
import com.sc2006.g5.edufinder.service.UserService;
import com.sc2006.g5.edufinder.setup.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        AuthFilter.class,
        SecurityConfig.class
    })
})
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private static final long EXISTED_USER_ID = 1L;
    private static final long INVALID_USER_ID = 2L;

    private static final long SCHOOL_ID_1 = 11L;
    private static final long SCHOOL_ID_2 = 12L;
    private static final long INVALID_SCHOOL_ID = 13L;

    private static final String POSTAL_CODE = "postal_code";

    @Nested
    @DisplayName("PUT /api/users")
    class EditUserTest {

        private MockHttpServletRequestBuilder mockRawRequest(String content) {
            return put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(String postalCode) {
            return mockRawRequest("""
                    {"postalCode": "%s"}
                """.formatted(postalCode));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 200 with user response when request valid")
        void shouldReturn200WithValidUserResponse() throws Exception {
            UserResponse response = UserResponse.builder()
                .id(EXISTED_USER_ID)
                .postalCode(POSTAL_CODE)
                .build();

            when(userService.editUser(
                eq(EXISTED_USER_ID),
                argThat(request -> request.getPostalCode().equals(POSTAL_CODE))
            )).thenReturn(response);

            mockMvc.perform(mockRequest(POSTAL_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTED_USER_ID))
                .andExpect(jsonPath("$.postalCode").value(POSTAL_CODE));

            verify(userService, times(1)).editUser(any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(mockRawRequest("{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest("""
                {"pos": "pos"}
            """)).andExpect(status().isBadRequest());
        }

        @Test
        @WithMockCustomUser(id = INVALID_USER_ID)
        @DisplayName("should return 404 when user not found")
        void shouldReturn404WhenUserNotFound() throws Exception {
            when(userService.editUser(
                eq(INVALID_USER_ID),
                argThat(request -> request.getPostalCode().equals(POSTAL_CODE))
            )).thenAnswer(invocation -> {
                throw new UserNotFoundException(INVALID_USER_ID);
            });

            mockMvc.perform(mockRequest(POSTAL_CODE))
                .andExpect(status().isNotFound());

            verify(userService, times(1)).editUser(any(), any());
        }
    }

    @Nested
    @DisplayName("GET /api/users/saved-schools")
    class GetSavedSchoolIdsTest {
        private MockHttpServletRequestBuilder mockRequest() {
            return get("/api/users/saved-schools");
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 200 with saved school response when request valid")
        void shouldReturn200WithSavedSchoolResponse() throws Exception {
            SavedSchoolResponse response = SavedSchoolResponse.builder()
                .savedSchoolIds(List.of(SCHOOL_ID_1, SCHOOL_ID_2))
                .build();

            when(userService.getSavedSchoolIds(EXISTED_USER_ID))
                .thenReturn(response);

            mockMvc.perform(mockRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.savedSchoolIds[0]").value(SCHOOL_ID_1))
                .andExpect(jsonPath("$.savedSchoolIds[1]").value(SCHOOL_ID_2));

            verify(userService, times(1)).getSavedSchoolIds(any());
        }

        @Test
        @WithMockCustomUser(id = INVALID_USER_ID)
        @DisplayName("should return 404 when user not found")
        void shouldReturn404WhenUserNotFound() throws Exception {
            when(userService.getSavedSchoolIds(INVALID_USER_ID))
                .thenAnswer(invocation -> {
                    throw new UserNotFoundException(INVALID_USER_ID);
                });

            mockMvc.perform(mockRequest())
                .andExpect(status().isNotFound());

            verify(userService, times(1)).getSavedSchoolIds(any());
        }
    }

    @Nested
    @DisplayName("POST /api/users/saved-schools")
    class AddSavedSchoolTest {
        private MockHttpServletRequestBuilder mockRawRequest(String content) {
            return post("/api/users/saved-schools")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(Long schoolId) {
            return mockRawRequest("""
                {"schoolId": "%d"}
            """.formatted(schoolId));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 204 when request valid")
        void shouldReturn204WhenRequestValid() throws Exception {
            mockMvc.perform(mockRequest(SCHOOL_ID_1))
                .andExpect(status().isNoContent());

            verify(userService, times(1)).addSavedSchool(
                eq(EXISTED_USER_ID),
                argThat(request -> request.getSchoolId().equals(SCHOOL_ID_1)));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(mockRawRequest("{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest("""
                {"school": 11}
            """)).andExpect(status().isBadRequest());

            verify(userService, never()).addSavedSchool(any(), any());
        }

        @Test
        @WithMockCustomUser(id = INVALID_USER_ID)
        @DisplayName("should return 404 when user not found")
        void shouldReturn404WhenUserNotFound() throws Exception {
            doThrow(new UserNotFoundException(INVALID_USER_ID)).when(userService).
                addSavedSchool(eq(INVALID_USER_ID),
                argThat(request -> request.getSchoolId().equals(SCHOOL_ID_1)));

            mockMvc.perform(mockRequest(SCHOOL_ID_1))
                .andExpect(status().isNotFound());

            verify(userService, times(1)).addSavedSchool(any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 409 when user already saved school")
        void shouldReturn409WhenUserAlreadySavedSchool() throws Exception {
            doThrow(new UserAlreadySaveSchoolException(EXISTED_USER_ID, SCHOOL_ID_2)).when(userService).
                addSavedSchool(
                    eq(EXISTED_USER_ID),
                    argThat(request -> request.getSchoolId().equals(SCHOOL_ID_2)));

            mockMvc.perform(mockRequest(SCHOOL_ID_2))
                .andExpect(status().isConflict());

            verify(userService, times(1)).addSavedSchool(any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 404 when school not found")
        void shouldReturn404WhenSchoolNotFound() throws Exception {
            doThrow(new SchoolNotFoundException(INVALID_SCHOOL_ID)).when(userService).
                addSavedSchool(eq(EXISTED_USER_ID),
                argThat(request -> request.getSchoolId().equals(INVALID_SCHOOL_ID)));

            mockMvc.perform(mockRequest(INVALID_SCHOOL_ID))
                    .andExpect(status().isNotFound());

            verify(userService, times(1)).addSavedSchool(any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE /api/users/saved-schools")
    class RemoveSavedSchoolTest {
        private MockHttpServletRequestBuilder mockRawRequest(String content) {
            return delete("/api/users/saved-schools")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(Long schoolId) {
            return mockRawRequest("""
                {"schoolId": "%d"}
            """.formatted(schoolId));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 204 when request valid")
        void shouldReturn204WhenRequestValid() throws Exception {
            mockMvc.perform(mockRequest(SCHOOL_ID_1))
                .andExpect(status().isNoContent());

            verify(userService, times(1)).removeSavedSchool(
                eq(EXISTED_USER_ID),
                argThat(request -> request.getSchoolId().equals(SCHOOL_ID_1)));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(mockRawRequest("{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest("""
                {"school": 11}
            """)).andExpect(status().isBadRequest());

            verify(userService, never()).removeSavedSchool(any(), any());
        }

        @Test
        @WithMockCustomUser(id = INVALID_USER_ID)
        @DisplayName("should return 404 when user not found")
        void shouldReturn404WhenUserNotFound() throws Exception {
            doThrow(new UserNotFoundException(INVALID_USER_ID)).when(userService).
                removeSavedSchool(
                    eq(INVALID_USER_ID),
                    argThat(request -> request.getSchoolId().equals(SCHOOL_ID_1)));

            mockMvc.perform(mockRequest(SCHOOL_ID_1))
                .andExpect(status().isNotFound());

            verify(userService, times(1)).removeSavedSchool(any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 409 when user not save school")
        void shouldReturn409WhenUserNotSaveSchool() throws Exception {
            doThrow(new UserNotSaveSchoolException(EXISTED_USER_ID, SCHOOL_ID_2)).when(userService).
                removeSavedSchool(
                    eq(EXISTED_USER_ID),
                    argThat(request -> request.getSchoolId().equals(SCHOOL_ID_2)));

            mockMvc.perform(mockRequest(SCHOOL_ID_2))
                .andExpect(status().isConflict());

            verify(userService, times(1)).removeSavedSchool(any(), any());
        }
    }
}
