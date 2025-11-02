package com.sc2006.g5.edufinder.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import com.sc2006.g5.edufinder.dto.api.*;
import com.sc2006.g5.edufinder.mapper.SchoolMapper;
import com.sc2006.g5.edufinder.model.school.Cca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sc2006.g5.edufinder.model.school.ApiSchool;
import com.sc2006.g5.edufinder.service.ApiClientService;
import com.sc2006.g5.edufinder.util.ApiResponseParser;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ApiSchoolRepositoryImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiResponseParser apiResponseParser;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private ApiSchoolRepositoryImpl apiSchoolRepositoryImpl;

    private static final String API_DOMAIN = "https://data.gov.sg/";
    private static final String API_ENDPOINT = "api/action/datastore_search?resource_id=";

    private static final String GENERAL_INFORMATION_DATASET_ID = "d_688b934f82c1059ed0a6993d2a829089";
    private static final String SUBJECTS_DATASET_ID = "d_f1d144e423570c9d84dbc5102c2e664d";
    private static final String CCAS_DATASET_ID = "d_9aba12b5527843afb0b2e8e4ed6ac6bd";
    private static final String MOE_PROGRAMMES_DATASET_ID = "d_b0697d22a7837a4eddf72efb66a36fc2";

    private static final String NEXT_SCHOOL_API_ENDPOINT = "nextSchoolApiEndpoint";
    private static final String NEXT_SUBJECT_API_ENDPOINT = "nextSubjectApiEndpoint";
    private static final String NEXT_CCA_API_ENDPOINT = "nextCcaApiEndpoint";
    private static final String NEXT_PROGRAMMES_API_ENDPOINT = "nextProgrammeApiEndpoint";

    private static final String SCHOOL_JSON = "schoolJson";
    private static final String NEXT_SCHOOL_JSON = "nextSchoolJson";

    private static final String SUBJECT_JSON = "subjectJson";
    private static final String NEXT_SUBJECT_JSON = "nextSubjectJson";

    private static final String CCA_JSON = "ccaJson";
    private static final String NEXT_CCA_JSON = "nextCcaJson";

    private static final String PROGRAMME_JSON = "programmeJson";
    private static final String NEXT_PROGRAMME_JSON = "nextProgrammeJson";

    private static final String SCHOOL_NAME_1 = "school1";
    private static final String SCHOOL_NAME_2 = "school2";
    
    private static final String SUBJECT_1 = "subject1";
    private static final String SUBJECT_2 = "subject2";
    private static final String SUBJECT_3 = "subject3";
    
    private static final String CCA_NAME_1 = "cca1";
    private static final String CCA_TYPE_1 = "ccaType1";
    private static final String CCA_NAME_2 = "cca2";
    private static final String CCA_TYPE_2 = "ccaType2";
    
    private static final String PROGRAMME_1 = "Programme1";
    private static final String PROGRAMME_2 = "Programme2";
    
    @Nested
    @DisplayName("getAllApiSchools()")
    class GetAllApiSchoolsTests {

        @Test
        @DisplayName("should return api schools")
        void shouldReturnApiSchools() {
            SchoolRecord schoolRecord1 = SchoolRecord.builder()
                .name(SCHOOL_NAME_1)
                .build();
            
            SchoolRecord schoolRecord2 = SchoolRecord.builder()
                .name(SCHOOL_NAME_2)
                .build();
            
            ApiResponse<SchoolRecord> schoolResponse = mockResponse(List.of(schoolRecord1), 2, NEXT_SCHOOL_API_ENDPOINT);
            ApiResponse<SchoolRecord> nextSchoolResponse = mockResponse(List.of(schoolRecord2), 2, null);
            
            SubjectRecord subjectRecord1 = SubjectRecord.builder()
                .schoolName(SCHOOL_NAME_1)
                .subject(SUBJECT_1)
                .build();
            
            SubjectRecord subjectRecord2 = SubjectRecord.builder()
                .schoolName(SCHOOL_NAME_1)
                .subject(SUBJECT_2)
                .build();
            
            SubjectRecord subjectRecord3 = SubjectRecord.builder()
                .schoolName(SCHOOL_NAME_2)
                .subject(SUBJECT_3)
                .build();

            ApiResponse<SubjectRecord> subjectResponse = mockResponse(List.of(subjectRecord1, subjectRecord2), 3, NEXT_SUBJECT_API_ENDPOINT);
            ApiResponse<SubjectRecord> nextSubjectResponse = mockResponse(List.of(subjectRecord3), 3, null);
            
            CcaRecord ccaRecord1 = CcaRecord.builder()
                .schoolName(SCHOOL_NAME_1)
                .cca(CCA_NAME_1)
                .type(CCA_TYPE_1)
                .build();

            CcaRecord ccaRecord2 = CcaRecord.builder()
                .schoolName(SCHOOL_NAME_2)
                .cca(CCA_NAME_2)
                .type(CCA_TYPE_2)
                .build();
            
            ApiResponse<CcaRecord> ccaResponse = mockResponse(List.of(ccaRecord1), 2, NEXT_CCA_API_ENDPOINT);
            ApiResponse<CcaRecord> nextCcaResponse = mockResponse(List.of(ccaRecord2), 2, null);
            
            ProgrammeRecord programmeRecord1 = ProgrammeRecord.builder()
                .schoolName(SCHOOL_NAME_1)
                .programme(PROGRAMME_1)
                .build();
            
            ProgrammeRecord programmeRecord2 = ProgrammeRecord.builder()
                .schoolName(SCHOOL_NAME_2)
                .programme(PROGRAMME_2)
                .build();

            ApiResponse<ProgrammeRecord> programmeResponse = mockResponse(List.of(programmeRecord1, programmeRecord1), 3, NEXT_PROGRAMMES_API_ENDPOINT);
            ApiResponse<ProgrammeRecord> nextProgrammeResponse = mockResponse(List.of(programmeRecord2), 3, null);

            when(apiClientService.get(API_DOMAIN + API_ENDPOINT + GENERAL_INFORMATION_DATASET_ID, null))
                    .thenReturn(SCHOOL_JSON);

            when(apiClientService.get(API_DOMAIN + NEXT_SCHOOL_API_ENDPOINT, null))
                    .thenReturn(NEXT_SCHOOL_JSON);

            when(apiClientService.get(API_DOMAIN + API_ENDPOINT + SUBJECTS_DATASET_ID, null))
                    .thenReturn(SUBJECT_JSON);

            when(apiClientService.get(API_DOMAIN + NEXT_SUBJECT_API_ENDPOINT, null))
                    .thenReturn(NEXT_SUBJECT_JSON);

            when(apiClientService.get(API_DOMAIN + API_ENDPOINT + CCAS_DATASET_ID, null))
                    .thenReturn(CCA_JSON);

            when(apiClientService.get(API_DOMAIN + NEXT_CCA_API_ENDPOINT, null))
                    .thenReturn(NEXT_CCA_JSON);

            when(apiClientService.get(API_DOMAIN + API_ENDPOINT + MOE_PROGRAMMES_DATASET_ID, null))
                    .thenReturn(PROGRAMME_JSON);

            when(apiClientService.get(API_DOMAIN + NEXT_PROGRAMMES_API_ENDPOINT, null))
                    .thenReturn(NEXT_PROGRAMME_JSON);

            when(apiResponseParser.parseApiRecord(SCHOOL_JSON, SchoolRecord.class))
                    .thenReturn(schoolResponse);

            when(apiResponseParser.parseApiRecord(NEXT_SCHOOL_JSON, SchoolRecord.class))
                    .thenReturn(nextSchoolResponse);

            when(apiResponseParser.parseApiRecord(SUBJECT_JSON, SubjectRecord.class))
                    .thenReturn(subjectResponse);

            when(apiResponseParser.parseApiRecord(NEXT_SUBJECT_JSON, SubjectRecord.class))
                    .thenReturn(nextSubjectResponse);

            when(apiResponseParser.parseApiRecord(CCA_JSON, CcaRecord.class))
                    .thenReturn(ccaResponse);

            when(apiResponseParser.parseApiRecord(NEXT_CCA_JSON, CcaRecord.class))
                    .thenReturn(nextCcaResponse);

            when(apiResponseParser.parseApiRecord(PROGRAMME_JSON, ProgrammeRecord.class))
                    .thenReturn(programmeResponse);

            when(apiResponseParser.parseApiRecord(NEXT_PROGRAMME_JSON, ProgrammeRecord.class))
                    .thenReturn(nextProgrammeResponse);

            List<SchoolRecord> expectedSchoolRecords = List.of(schoolRecord1, schoolRecord2);
            Map<String, List<String>> expectedSubjectMap = Map.of(
                SCHOOL_NAME_1, List.of(SUBJECT_1, SUBJECT_2),
                SCHOOL_NAME_2, List.of(SUBJECT_3)
            );
            Map<String, List<Cca>> expectedCcaMap = Map.of(
                SCHOOL_NAME_1, List.of(new Cca(CCA_NAME_1, CCA_TYPE_1)),
                SCHOOL_NAME_2, List.of(new Cca(CCA_NAME_2, CCA_TYPE_2))
            );
            Map<String, List<String>> expectedProgrammeMap = Map.of(
                SCHOOL_NAME_1, List.of(PROGRAMME_1),
                SCHOOL_NAME_2, List.of(PROGRAMME_2)
            );

            ApiSchool apiSchool1 = ApiSchool.builder()
                .name(SCHOOL_NAME_1)
                .build();

            ApiSchool apiSchool2 = ApiSchool.builder()
                .name(SCHOOL_NAME_2)
                .build();

            when(schoolMapper.toApiSchools(
                argThat(schoolRecords -> schoolRecords.equals(expectedSchoolRecords)),
                argThat(subjectMap -> subjectMap.equals(expectedSubjectMap)),
                argThat(ccaMap -> ccaMap.equals(expectedCcaMap)),
                argThat(programmeMap -> programmeMap.equals(expectedProgrammeMap))
            )).thenReturn(List.of(apiSchool1, apiSchool2));

            List<ApiSchool> schools = apiSchoolRepositoryImpl.getApiSchools();
            assertEquals(List.of(apiSchool1, apiSchool2), schools);

            verify(apiClientService, times(8)).get(any(), any());
            verify(apiResponseParser, times(8)).parseApiRecord(any(), any());
            verify(schoolMapper, times(1)).toApiSchools(any(), any(), any(), any());

            apiSchoolRepositoryImpl.getApiSchools(); // Test cached
            verify(apiClientService, times(8)).get(any(), any());
            verify(apiResponseParser, times(8)).parseApiRecord(any(), any());
            verify(schoolMapper, times(1)).toApiSchools(any(), any(), any(), any());
        }
        
        private <R extends ApiRecord> ApiResponse<R> mockResponse(List<R> records, int total, String next) {
            ApiLink link = ApiLink.builder()
                .next(next)
                .build();
            
            ApiResult<R> result = ApiResult.<R>builder()
                .records(records)
                .link(link)
                .total(total)
                .build();
            
            return ApiResponse.<R>builder()
                .success(true)
                .result(result)
                .build();
        }
    }
}
