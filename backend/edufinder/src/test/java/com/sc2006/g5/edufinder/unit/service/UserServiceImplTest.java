package com.sc2006.g5.edufinder.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.sc2006.g5.edufinder.dto.request.EditUserRequest;
import com.sc2006.g5.edufinder.dto.response.UserResponse;
import com.sc2006.g5.edufinder.mapper.UserMapper;
import com.sc2006.g5.edufinder.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sc2006.g5.edufinder.dto.request.SavedSchoolRequest;
import com.sc2006.g5.edufinder.dto.response.SavedSchoolResponse;
import com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException;
import com.sc2006.g5.edufinder.exception.user.UserAlreadySaveSchoolException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.exception.user.UserNotSaveSchoolException;
import com.sc2006.g5.edufinder.model.school.DbSchool;
import com.sc2006.g5.edufinder.model.user.User;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;
import com.sc2006.g5.edufinder.repository.UserSavedSchoolRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    DbSchoolRepository schoolRepository;

    @Mock
    UserSavedSchoolRepository userSavedSchoolRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    private static final Long EXISTED_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 2L;

    private static final Long SAVED_SCHOOL_ID_1 = 11L;
    private static final Long SAVED_SCHOOL_ID_2 = 12L;
    private static final Long NEW_SAVED_SCHOOL_ID = 13L;
    private static final Long INVALID_SCHOOL_ID = 14L;

    @BeforeEach
    void setup(){
        lenient().when(userRepository.existsById(EXISTED_USER_ID))
            .thenReturn(true);

        lenient().when(userRepository.existsById(INVALID_USER_ID))
            .thenReturn(false);

        lenient().when(userSavedSchoolRepository.existsByUserIdAndSchoolId(anyLong(), anyLong()))
            .thenAnswer(invocation -> {
                Long userId = invocation.getArgument(0);
                Long schoolId = invocation.getArgument(1);

                return userId.equals(EXISTED_USER_ID) && (
                    schoolId.equals(SAVED_SCHOOL_ID_1) ||
                    schoolId.equals(SAVED_SCHOOL_ID_2)
                );
            });
    }

    @Nested
    @DisplayName("getUserByUsername()")
    class GetUserByUsernameTest {

        private static final String EXISTED_USERNAME = "user";
        private static final String INVALID_USERNAME = "invalid";

        @Test
        @DisplayName("shouldReturnUserWhenUsernameExisted")
        void shouldReturnUserWhenUsernameExisted() {
            User user = User.builder()
                .id(EXISTED_USER_ID)
                .username(EXISTED_USERNAME)
                .build();

            UserResponse expectedResponse = UserResponse.builder()
                .id(EXISTED_USER_ID)
                .username(EXISTED_USERNAME)
                .build();

            when(userRepository.findOneByUsername(EXISTED_USERNAME))
                .thenReturn(Optional.of(user));

            when(userMapper.toUserResponse(user))
                .thenReturn(expectedResponse);

            UserResponse serviceResponse = userServiceImpl.getUserByUsername(EXISTED_USERNAME);

            assertEquals(expectedResponse, serviceResponse);

            verify(userRepository, times(1)).findOneByUsername(any());
            verify(userMapper, times(1)).toUserResponse(any());
        }

        @Test
        @DisplayName("should throw when username not existed")
        void shouldThrowWhenUsernameNotExisted() {
            when(userRepository.findOneByUsername(INVALID_USERNAME))
                .thenReturn(Optional.empty());

            assertThrowsExactly(UserNotFoundException.class, () ->
                userServiceImpl.getUserByUsername(INVALID_USERNAME)
            );

            verify(userRepository, times(1)).findOneByUsername(any());
            verify(userMapper, never()).toUserResponse(any());
        }
    }

    @Nested
    @DisplayName("editUser()")
    class EditUserTest {

        String OLD_POSTAL_CODE = "old_postal_code";
        String NEW_POSTAL_CODE = "new_postal_code";

        @Test
        @DisplayName("should edit and return user response when request valid")
        void shouldEditAndReturnUserResponseWhenRequestValid() {
            User user = User.builder()
                .id(EXISTED_USER_ID)
                .postalCode(OLD_POSTAL_CODE)
                .build();

            UserResponse expectedResponse = UserResponse.builder()
                .id(EXISTED_USER_ID)
                .build();

            when(userRepository.findById(EXISTED_USER_ID))
                .thenReturn(Optional.of(user));

            when(userRepository.save(argThat(editedUser ->
                editedUser.getId().equals(EXISTED_USER_ID) &&
                editedUser.getPostalCode().equals(NEW_POSTAL_CODE)
            ))).thenAnswer(invocation -> invocation.getArgument(0));

            when(userMapper.toUserResponse(argThat(editedUser ->
                editedUser.getId().equals(EXISTED_USER_ID) &&
                editedUser.getPostalCode().equals(NEW_POSTAL_CODE)
            ))).thenReturn(expectedResponse);

            EditUserRequest request = EditUserRequest.builder()
                .postalCode(NEW_POSTAL_CODE)
                .build();

            UserResponse serviceResponse = userServiceImpl.editUser(EXISTED_USER_ID, request);

            assertEquals(expectedResponse, serviceResponse);

            verify(userRepository, times(1)).findById(any());
            verify(userRepository, times(1)).save(any());
            verify(userMapper, times(1)).toUserResponse(any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(INVALID_USER_ID))
                .thenReturn(Optional.empty());

            EditUserRequest request = EditUserRequest.builder()
                .postalCode(NEW_POSTAL_CODE)
                .build();


            assertThrowsExactly(UserNotFoundException.class, () ->
                userServiceImpl.editUser(INVALID_USER_ID, request)
            );

            verify(userRepository, times(1)).findById(any());
            verify(userRepository, never()).save(any());
            verify(userMapper, never()).toUserResponse(any());
        }
    }

    @Nested
    @DisplayName("getSavedSchoolsId()")
    class GetSavedSchoolsIdTest {

        @Test
        @DisplayName("should return saved school ids when request is valid")
        void shouldReturnSavedSchoolIdsWhenRequestValid(){
            when(userSavedSchoolRepository.findSchoolIdsByUserId(EXISTED_USER_ID))
                .thenReturn(List.of(SAVED_SCHOOL_ID_1, SAVED_SCHOOL_ID_2));

            SavedSchoolResponse response = userServiceImpl.getSavedSchoolIds(EXISTED_USER_ID);
            List<Long> savedSchoolIds = response.getSavedSchoolIds();
            
            assertEquals(2, savedSchoolIds.size());
            assertEquals(SAVED_SCHOOL_ID_1, savedSchoolIds.get(0));
            assertEquals(SAVED_SCHOOL_ID_2, savedSchoolIds.get(1));

            verify(userRepository, times(1)).existsById(any());
            verify(userSavedSchoolRepository, times(1)).findSchoolIdsByUserId(any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound(){
            assertThrowsExactly(UserNotFoundException.class, () -> 
                userServiceImpl.getSavedSchoolIds(INVALID_USER_ID)
            );

            verify(userRepository, times(1)).existsById(any());
            verify(userSavedSchoolRepository, never()).findSchoolIdsByUserId(any());
        }
    }

    @Nested
    @DisplayName("addSavedSchool()")
    class addSavedSchoolTest {

        @BeforeEach
        void setup(){
            final User user = User.builder()
                    .id(EXISTED_USER_ID)
                    .build();

            lenient().when(userRepository.findById(EXISTED_USER_ID))
                    .thenReturn(Optional.of(user));
        }

        @Test
        @DisplayName("should add saved school when request valid")
        void shouldSaveSchoolWhenIsSaving(){
            final DbSchool newSavedSchool = DbSchool.builder()
                .id(NEW_SAVED_SCHOOL_ID)
                .build();

            when(schoolRepository.findById(NEW_SAVED_SCHOOL_ID))
                .thenReturn(Optional.of(newSavedSchool));

            when(userSavedSchoolRepository.save(argThat(userSavedSchool -> 
                userSavedSchool.getUser().getId().equals(EXISTED_USER_ID) &&
                userSavedSchool.getSchool().getId().equals(NEW_SAVED_SCHOOL_ID)
            ))).thenAnswer(invocation -> invocation.getArgument(0));
            
            SavedSchoolRequest request = SavedSchoolRequest.builder()
                .schoolId(NEW_SAVED_SCHOOL_ID)
                .build();

            userServiceImpl.addSavedSchool(EXISTED_USER_ID, request);
    
            verify(userRepository, times(1)).findById(any());
            verify(schoolRepository, times(1)).findById(any());
            verify(userSavedSchoolRepository, times(1)).existsByUserIdAndSchoolId(any(), any());
            verify(userSavedSchoolRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound(){
            when(userRepository.findById(INVALID_USER_ID))
                    .thenReturn(Optional.empty());

            SavedSchoolRequest request = SavedSchoolRequest.builder()
                .schoolId(NEW_SAVED_SCHOOL_ID)
                .build();

            assertThrowsExactly(UserNotFoundException.class, () -> 
                userServiceImpl.addSavedSchool(INVALID_USER_ID, request)
            );

            verify(userRepository, times(1)).findById(any());
            verify(schoolRepository, never()).findById(any());
            verify(userSavedSchoolRepository, never()).existsByUserIdAndSchoolId(any(), any());
            verify(userSavedSchoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw when school already saved")
        void shouldThrowWhenSchoolAlreadySaved(){
            SavedSchoolRequest request = SavedSchoolRequest.builder()
                .schoolId(SAVED_SCHOOL_ID_1)
                .build();

            assertThrowsExactly(UserAlreadySaveSchoolException.class, () -> 
                userServiceImpl.addSavedSchool(EXISTED_USER_ID, request)
            );

            verify(userRepository, times(1)).findById(any());
            verify(schoolRepository, never()).findById(any());
            verify(userSavedSchoolRepository, times(1)).existsByUserIdAndSchoolId(any(), any());
            verify(userSavedSchoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw when school not found")
        void shouldThrowWhenSchoolNotFound(){
            SavedSchoolRequest request = SavedSchoolRequest.builder()
                .schoolId(INVALID_SCHOOL_ID)
                .build();
            
            assertThrowsExactly(SchoolNotFoundException.class, () -> 
                userServiceImpl.addSavedSchool(EXISTED_USER_ID, request)
            );

            verify(userRepository, times(1)).findById(any());
            verify(schoolRepository, times(1)).findById(any());
            verify(userSavedSchoolRepository, times(1)).existsByUserIdAndSchoolId(any(), any());
            verify(userSavedSchoolRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeSavedSchool()")
    class removeSavedSchoolTest {

        @Test
        @DisplayName("should remove saved school when request valid")
        void shouldRemoveSavedSchoolWhenRequestValid(){
            doNothing().when(userSavedSchoolRepository).deleteByUserIdAndSchoolId(
                argThat(userId -> userId.equals(EXISTED_USER_ID)),
                argThat(schoolId -> schoolId.equals(SAVED_SCHOOL_ID_1))
            );

            SavedSchoolRequest request = SavedSchoolRequest.builder()
                .schoolId(SAVED_SCHOOL_ID_1)
                .build();

            userServiceImpl.removeSavedSchool(EXISTED_USER_ID, request);

            verify(userRepository, times(1)).existsById(any());
            verify(userSavedSchoolRepository, times(1)).existsByUserIdAndSchoolId(any(), any());
            verify(userSavedSchoolRepository, times(1)).deleteByUserIdAndSchoolId(any(), any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound(){
            SavedSchoolRequest request = SavedSchoolRequest.builder()
                .schoolId(SAVED_SCHOOL_ID_1)
                .build();

            assertThrowsExactly(UserNotFoundException.class, () -> 
                userServiceImpl.removeSavedSchool(INVALID_USER_ID, request)
            );

            verify(userRepository, times(1)).existsById(any());
            verify(userSavedSchoolRepository, never()).existsByUserIdAndSchoolId(any(), any());
            verify(userSavedSchoolRepository, never()).deleteByUserIdAndSchoolId(any(), any());
        }

        @Test
        @DisplayName("should throw when school not saved")
        void shouldThrowWhenUserNotSaveSchool(){
            SavedSchoolRequest request = SavedSchoolRequest.builder()
                .schoolId(NEW_SAVED_SCHOOL_ID)
                .build();

            assertThrowsExactly(UserNotSaveSchoolException.class, () -> 
                userServiceImpl.removeSavedSchool(EXISTED_USER_ID, request)
            );

            verify(userRepository, times(1)).existsById(any());
            verify(userSavedSchoolRepository, times(1)).existsByUserIdAndSchoolId(any(), any());
            verify(userSavedSchoolRepository, never()).deleteByUserIdAndSchoolId(any(), any());
        }
    }
}
