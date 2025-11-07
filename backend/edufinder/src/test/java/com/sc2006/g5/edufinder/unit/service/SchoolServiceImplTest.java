package com.sc2006.g5.edufinder.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.sc2006.g5.edufinder.service.SchoolServiceImpl;
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
}
