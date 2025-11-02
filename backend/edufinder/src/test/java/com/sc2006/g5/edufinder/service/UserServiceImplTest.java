package com.sc2006.g5.edufinder.service;

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
import com.sc2006.g5.edufinder.model.User;
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

    @InjectMocks
    UserServiceImpl userServiceImpl;

    private static final Long EXISTED_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 2L;

    private static final Long SAVED_SCHOOL_ID_1 = 1L;
    private static final Long SAVED_SCHOOL_ID_2 = 2L;
    private static final Long NEW_SAVED_SCHOOL_ID = 3L;
    private static final Long INVALID_SCHOOL_ID = 4L;

    @BeforeEach
    void setup(){
        final User user = User.builder()
            .id(EXISTED_USER_ID)
            .build();

        lenient().when(userRepository.existsById(EXISTED_USER_ID))
            .thenReturn(true);

        lenient().when(userRepository.existsById(INVALID_USER_ID))
            .thenReturn(false);

        lenient().when(userRepository.findById(EXISTED_USER_ID))
            .thenReturn(Optional.of(user));

        lenient().when(userRepository.findById(INVALID_USER_ID))
            .thenReturn(Optional.empty());

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
