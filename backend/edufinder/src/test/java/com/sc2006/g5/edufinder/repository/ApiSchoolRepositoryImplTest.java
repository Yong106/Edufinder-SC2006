package com.sc2006.g5.edufinder.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sc2006.g5.edufinder.dto.api.ApiLink;
import com.sc2006.g5.edufinder.dto.api.ApiResponse;
import com.sc2006.g5.edufinder.dto.api.ApiResult;
import com.sc2006.g5.edufinder.dto.api.CcaRecord;
import com.sc2006.g5.edufinder.dto.api.SchoolRecord;
import com.sc2006.g5.edufinder.dto.api.SubjectRecord;
import com.sc2006.g5.edufinder.model.ApiSchool;
import com.sc2006.g5.edufinder.service.ApiClientService;
import com.sc2006.g5.edufinder.util.ApiResponseParser;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ApiSchoolRepositoryImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiResponseParser apiResponseParser;

    @InjectMocks
    private ApiSchoolRepositoryImpl apiSchoolRepositoryImpl;

    @Test
    void getApiSchools_test(){
        final String apiDomain = "https://data.gov.sg/";
        final String apiEndpoint = "api/action/datastore_search?resource_id=";
        final String generalInformationDatasetId = "d_688b934f82c1059ed0a6993d2a829089";
        final String subjectsDatasetId = "d_f1d144e423570c9d84dbc5102c2e664d";
        final String ccasDatasetId = "d_9aba12b5527843afb0b2e8e4ed6ac6bd";

        final String nextSchoolApiEndpoint = "nextSchoolApiEndpoint";
        final String nextSubjectApiEndpoint = "nextSubjectApiEndpoint";
        final String nextCcaApiEndpoint = "nextCcaApiEndpoint";

        final String schoolJson = "schoolJson";
        final String nextSchoolJson = "nextSchoolJson";

        final String subjectJson = "subjectJson";
        final String nextSubjectJson = "nextSubjectJson";

        final String ccaJson = "ccaJson";
        final String nextCcaJson = "nextCcaJson";

        final SchoolRecord schoolRecord1 = SchoolRecord.builder()
            .name("name1")
            .location("location1")
            .address("address1")
            .postalCode("postal1")
            .website("web1")
            .email("email1")
            .phoneNumber("phone1")
            .faxNumber("fax1")
            .nearbyMrtStation("mrt1, mrt2")
            .nearbyBusStation("bus1, bus2")
            .level("level1")
            .natureCode("n1")
            .type("type1")
            .sessionCode("s1")
            .build();

        final SchoolRecord schoolRecord2 = SchoolRecord.builder()
            .name("name2")
            .location("location2")
            .address("address2")
            .postalCode("postal2")
            .website("web2")
            .email("email2")
            .phoneNumber("phone2")
            .faxNumber("fax2")
            .nearbyMrtStation("mrt2, mrt3")
            .nearbyBusStation("bus2, bus3")
            .level("level2")
            .natureCode("n2")
            .type("type2")
            .sessionCode("s2")
            .build();

        final SchoolRecord schoolRecord3 = SchoolRecord.builder()
            .name("name3")
            .location("location3")
            .address("address3")
            .postalCode("postal3")
            .website("web3")
            .email("email3")
            .phoneNumber("phone3")
            .faxNumber("fax3")
            .nearbyMrtStation("mrt2, mrt3")
            .nearbyBusStation("bus2, bus3")
            .level("level3")
            .natureCode("n3")
            .type("type3")
            .sessionCode("s3")
            .build();

        final ApiLink schoolLink = ApiLink.builder()
            .next(nextSchoolApiEndpoint)
            .build();
        
        final ApiResult<SchoolRecord> schoolResult = ApiResult.<SchoolRecord>builder()
            .records(List.of(schoolRecord1, schoolRecord2))
            .link(schoolLink)
            .total(3)
            .build();
            
        final ApiResponse<SchoolRecord> schoolResponse = ApiResponse.<SchoolRecord>builder()
            .success(true)
            .result(schoolResult)
            .build();

        final ApiLink nextSchoolLink = ApiLink.builder()
            .next(null)
            .build();

        final ApiResult<SchoolRecord> nextSchoolResult = ApiResult.<SchoolRecord>builder()
            .records(List.of(schoolRecord3))
            .link(nextSchoolLink)
            .total(3)
            .build();
            
        final ApiResponse<SchoolRecord> nextSchoolResponse = ApiResponse.<SchoolRecord>builder()
            .success(true)
            .result(nextSchoolResult)
            .build();
        
        final SubjectRecord subjectRecord1 = SubjectRecord.builder()
            .schoolName(schoolRecord1.getName())
            .subject("subject1")
            .build();

        final SubjectRecord subjectRecord2 = SubjectRecord.builder()
            .schoolName(schoolRecord1.getName())
            .subject("subject2")
            .build();

        final SubjectRecord subjectRecord3 = SubjectRecord.builder()
            .schoolName(schoolRecord1.getName())
            .subject("subject3")
            .build();

        final ApiLink nextSubjectLink = ApiLink.builder()
            .next(nextSubjectApiEndpoint)
            .build();

        final ApiResult<SubjectRecord> subjectResult = ApiResult.<SubjectRecord>builder()
            .records(List.of(subjectRecord1, subjectRecord2))
            .link(nextSubjectLink)
            .total(3)
            .build();
            
        final ApiResponse<SubjectRecord> subjectResponse = ApiResponse.<SubjectRecord>builder()
            .success(true)
            .result(subjectResult)
            .build();

        final ApiResult<SubjectRecord> nextSubjectResult = ApiResult.<SubjectRecord>builder()
            .records(List.of(subjectRecord3))
            .link(nextSubjectLink)
            .total(3)
            .build();

        final ApiResponse<SubjectRecord> nextSubjectResponse = ApiResponse.<SubjectRecord>builder()
            .success(true)
            .result(nextSubjectResult)
            .build();

        
        final CcaRecord ccaRecord1 = CcaRecord.builder()
            .schoolName(schoolRecord1.getName())
            .cca("cca1")
            .build();

        final CcaRecord ccaRecord2 = CcaRecord.builder()
            .schoolName(schoolRecord1.getName())
            .cca("cca2")
            .build();

        final CcaRecord ccaRecord3 = CcaRecord.builder()
            .schoolName(schoolRecord1.getName())
            .cca("cca3")
            .build();

        final ApiLink nextCcaLink = ApiLink.builder()
            .next(nextCcaApiEndpoint)
            .build();

        final ApiResult<CcaRecord> ccaResult = ApiResult.<CcaRecord>builder()
            .records(List.of(ccaRecord1, ccaRecord2))
            .link(nextCcaLink)
            .total(3)
            .build();
            
        final ApiResponse<CcaRecord> ccaResponse = ApiResponse.<CcaRecord>builder()
            .success(true)
            .result(ccaResult)
            .build();

        final ApiResult<CcaRecord> nextCcaResult = ApiResult.<CcaRecord>builder()
            .records(List.of(ccaRecord3))
            .link(nextCcaLink)
            .total(3)
            .build();
            
        final ApiResponse<CcaRecord> nextCcaResponse = ApiResponse.<CcaRecord>builder()
            .success(true)
            .result(nextCcaResult)
            .build();


        when(apiClientService.get(apiDomain + apiEndpoint + generalInformationDatasetId, null))
            .thenReturn(schoolJson);

        when(apiClientService.get(apiDomain + nextSchoolApiEndpoint, null))
            .thenReturn(nextSchoolJson);

        when(apiClientService.get(apiDomain + apiEndpoint + subjectsDatasetId, null))
            .thenReturn(subjectJson);

        when(apiClientService.get(apiDomain + nextSubjectApiEndpoint, null))
            .thenReturn(nextSubjectJson);

        when(apiClientService.get(apiDomain + apiEndpoint +  ccasDatasetId, null))
            .thenReturn(ccaJson);

         when(apiClientService.get(apiDomain + nextCcaApiEndpoint, null))
            .thenReturn(nextCcaJson);
        
        when(apiResponseParser.parseApiRecord(schoolJson, SchoolRecord.class))
            .thenReturn(schoolResponse);

        when(apiResponseParser.parseApiRecord(nextSchoolJson, SchoolRecord.class))
            .thenReturn(nextSchoolResponse);

        when(apiResponseParser.parseApiRecord(subjectJson, SubjectRecord.class))
            .thenReturn(subjectResponse);
        
        when(apiResponseParser.parseApiRecord(nextSubjectJson, SubjectRecord.class))
            .thenReturn(nextSubjectResponse);
        
        when(apiResponseParser.parseApiRecord(ccaJson, CcaRecord.class))
            .thenReturn(ccaResponse);

        when(apiResponseParser.parseApiRecord(nextCcaJson, CcaRecord.class))
            .thenReturn(nextCcaResponse);

        List<ApiSchool> schools = apiSchoolRepositoryImpl.getApiSchools();
        assertEquals(3, schools.size());

        ApiSchool school = schools.get(0);
        assertEquals(schoolRecord1.getName(), school.getName());
        assertEquals(schoolRecord1.getLocation(), school.getLocation());
        assertEquals(schoolRecord1.getAddress(), school.getAddress());
        assertEquals(schoolRecord1.getPostalCode(), school.getPostalCode());
        assertEquals(schoolRecord1.getWebsite(), school.getWebsite());
        assertEquals(schoolRecord1.getEmail(), school.getEmail());
        assertEquals(schoolRecord1.getPhoneNumber(), school.getPhoneNumber());
        assertEquals(schoolRecord1.getFaxNumber(), school.getFaxNumber());
        assertEquals(schoolRecord1.getLevel(), school.getLevel());
        assertEquals(schoolRecord1.getNatureCode(), school.getNatureCode());
        assertEquals(schoolRecord1.getType(), school.getType());
        assertEquals(schoolRecord1.getSessionCode(), school.getSessionCode());
        assertEquals(schoolRecord1.getNearbyBusStation(), school.getNearbyBusStation());
        assertEquals(schoolRecord1.getNearbyMrtStation(), school.getNearbyMrtStation());

        List<String> subjects = school.getSubjects();
        assertEquals(3, subjects.size());
        assertEquals(subjectRecord1.getSubject(), subjects.get(0));
        assertEquals(subjectRecord2.getSubject(), subjects.get(1));
        assertEquals(subjectRecord3.getSubject(), subjects.get(2));

        List<String> ccas = school.getCcas();
        assertEquals(3, ccas.size());
        assertEquals(ccaRecord1.getCca(), ccas.get(0));
        assertEquals(ccaRecord2.getCca(), ccas.get(1));
        assertEquals(ccaRecord3.getCca(), ccas.get(2));
    }
}
