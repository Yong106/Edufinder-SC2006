package com.sc2006.g5.edufinder.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.sc2006.g5.edufinder.dto.request.EditSchoolCutOffPointRequest;
import com.sc2006.g5.edufinder.exception.school.CutOffPointException;
import com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException;
import com.sc2006.g5.edufinder.service.SchoolServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sc2006.g5.edufinder.dto.response.SchoolResponse;
import com.sc2006.g5.edufinder.dto.response.SchoolsResponse;
import com.sc2006.g5.edufinder.mapper.SchoolMapper;
import com.sc2006.g5.edufinder.model.school.ApiSchool;
import com.sc2006.g5.edufinder.model.school.DbSchool;
import com.sc2006.g5.edufinder.repository.ApiSchoolRepository;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceImplTest {

    @Mock
    private ApiSchoolRepository apiSchoolRepository;

    @Mock
    private DbSchoolRepository dbSchoolRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private SchoolServiceImpl schoolServiceImpl;

    private static final Long SCHOOL_ID_1 = 1L;
    private static final Long SCHOOL_ID_2 = 2L;

    private static final String SCHOOL_NAME_1 = "name1";
    private static final String SCHOOL_NAME_2 = "name2";

    @Nested
    @DisplayName("getAllSchools()")
    class GetAllSchoolsTest{

        @Test
        @DisplayName("should return all school when request valid")
        void shouldReturnAllSchoolWhenRequestValid(){
            final ApiSchool apiSchool1 = ApiSchool.builder()
                    .name(SCHOOL_NAME_1)
                    .build();

            final ApiSchool apiSchool2 = ApiSchool.builder()
                    .name(SCHOOL_NAME_2)
                    .build();

            final DbSchool dbSchool1 = DbSchool.builder()
                    .id(SCHOOL_ID_1)
                    .name(SCHOOL_NAME_1)
                    .build();

            when(apiSchoolRepository.getApiSchools())
                    .thenReturn(List.of(apiSchool1, apiSchool2));

            when(dbSchoolRepository.findOneByName(SCHOOL_NAME_1))
                    .thenReturn(Optional.of(dbSchool1));

            when(dbSchoolRepository.findOneByName(SCHOOL_NAME_2))
                    .thenReturn(Optional.empty());

            when(dbSchoolRepository.save(argThat(dbSchool ->
                    dbSchool.getName().equals(apiSchool2.getName()) &&
                            dbSchool.getMinCutOffPoint() == null &&
                            dbSchool.getMaxCutOffPoint() == null
            ))).thenAnswer(invocation -> {
                DbSchool dbSchool = invocation.getArgument(0);
                dbSchool.setId(SCHOOL_ID_2);
                return dbSchool;
            });

            when(schoolMapper.toSchoolResponse(any(ApiSchool.class), any(DbSchool.class)))
                    .thenAnswer(invocation -> {
                        ApiSchool apiSchool = invocation.getArgument(0);
                        DbSchool dbSchool = invocation.getArgument(1);

                        return SchoolResponse.builder()
                                .id(dbSchool.getId())
                                .name(apiSchool.getName())
                                .build();
                    });

            SchoolsResponse schoolsResponse = schoolServiceImpl.getAllSchools();
            List<SchoolResponse> schoolResponses = schoolsResponse.getSchools();
            assertEquals(2, schoolResponses.size());

            SchoolResponse schoolResponse1 = schoolResponses.get(0);
            assertEquals(SCHOOL_ID_1, schoolResponse1.getId());
            assertEquals(SCHOOL_NAME_1, schoolResponse1.getName());

            SchoolResponse schoolResponse2 = schoolResponses.get(1);
            assertEquals(SCHOOL_ID_2, schoolResponse2.getId());
            assertEquals(SCHOOL_NAME_2, schoolResponse2.getName());

            verify(apiSchoolRepository, times(1)).getApiSchools();
            verify(dbSchoolRepository, times(2)).findOneByName(any());
            verify(dbSchoolRepository, times(1)).save(any());
            verify(schoolMapper, times(2)).toSchoolResponse(any(), any());
        }
    }

    @Nested
    @DisplayName("editSchoolCutOffPoint()")
    class EditSchoolCutOffPointTest{

        private static final long INVALID_SCHOOL_ID = 3L;

        private static final int MIN_CUT_OFF_POINT = 4;
        private static final int MAX_CUT_OFF_POINT = 32;

        @BeforeEach
        void setup(){
            DbSchool school = DbSchool.builder()
                .id(SCHOOL_ID_1)
                .minCutOffPoint(null)
                .maxCutOffPoint(null)
                .build();

            lenient().when(dbSchoolRepository.findById(SCHOOL_ID_1))
                .thenReturn(Optional.of(school));
        }

        @Test
        @DisplayName("should edit school cut off point if request valid")
        void shouldEditSchoolCutOffPointWhenRequestValid(){
            when(dbSchoolRepository.save(argThat(dbSchool ->
                dbSchool.getId().equals(SCHOOL_ID_1) &&
                dbSchool.getMinCutOffPoint().equals(MIN_CUT_OFF_POINT) &&
                dbSchool.getMaxCutOffPoint().equals(MAX_CUT_OFF_POINT)
            ))).thenAnswer(invocation -> invocation.getArgument(0));

            EditSchoolCutOffPointRequest request = EditSchoolCutOffPointRequest.builder()
                .minCutOffPoint(MIN_CUT_OFF_POINT)
                .maxCutOffPoint(MAX_CUT_OFF_POINT)
                .build();

            schoolServiceImpl.editSchoolCutOffPoint(SCHOOL_ID_1, request);

            verify(dbSchoolRepository, times(1)).findById(any());
            verify(dbSchoolRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("should throw when school not found")
        void shouldThrowWhenSchoolNotFound(){
            when(dbSchoolRepository.findById(INVALID_SCHOOL_ID))
                .thenReturn(Optional.empty());

            EditSchoolCutOffPointRequest request = EditSchoolCutOffPointRequest.builder()
                .minCutOffPoint(MIN_CUT_OFF_POINT)
                .maxCutOffPoint(MAX_CUT_OFF_POINT)
                .build();

            assertThrowsExactly(SchoolNotFoundException.class, () ->
                schoolServiceImpl.editSchoolCutOffPoint(INVALID_SCHOOL_ID, request)
            );

            verify(dbSchoolRepository, times(1)).findById(any());
            verify(dbSchoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw when min is larger than max")
        void shouldThrowWhenMinIsLargerThanMax(){
            EditSchoolCutOffPointRequest request = EditSchoolCutOffPointRequest.builder()
                .minCutOffPoint(MAX_CUT_OFF_POINT)
                .maxCutOffPoint(MIN_CUT_OFF_POINT)
                .build();

            assertThrowsExactly(CutOffPointException.class, () ->
                schoolServiceImpl.editSchoolCutOffPoint(SCHOOL_ID_1, request)
            );

            verify(dbSchoolRepository, times(1)).findById(any());
            verify(dbSchoolRepository, never()).save(any());
        }
    }
}
