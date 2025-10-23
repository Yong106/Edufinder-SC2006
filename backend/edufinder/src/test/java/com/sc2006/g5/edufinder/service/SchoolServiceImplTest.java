package com.sc2006.g5.edufinder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sc2006.g5.edufinder.dto.response.SchoolResponse;
import com.sc2006.g5.edufinder.model.ApiSchool;
import com.sc2006.g5.edufinder.model.DbSchool;
import com.sc2006.g5.edufinder.repository.ApiSchoolRepository;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceImplTest {
    @Mock
    private ApiSchoolRepository apiSchoolRepository;

    @Mock
    private DbSchoolRepository dbSchoolRepository;

    @InjectMocks
    private SchoolServiceImpl schoolServiceImpl;

    @Test
    void getAllSchools_test(){
        final ApiSchool apiSchool1 = ApiSchool.builder()
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
            .ccas(List.of("cca1", "cca2"))
            .subjects(List.of("subject1", "subject2"))
            .build();

        final ApiSchool apiSchool2 = ApiSchool.builder()
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
            .ccas(List.of("cca2", "cca3"))
            .subjects(List.of("subject2", "subject3"))
            .build();
        
        final DbSchool dbSchool1 = DbSchool.builder()
            .id(1L)
            .name(apiSchool1.getName())
            .minCutOffPoint(1)
            .maxCutOffPoint(5)
            .build();

        when(apiSchoolRepository.getApiSchools())
            .thenReturn(List.of(apiSchool1, apiSchool2));
        
        when(dbSchoolRepository.findByName(apiSchool1.getName()))
            .thenReturn(List.of(dbSchool1));
        
        when(dbSchoolRepository.save(argThat(dbSchool ->
            dbSchool.getName().equals(apiSchool2.getName()) &&
            dbSchool.getMinCutOffPoint() == null && 
            dbSchool.getMaxCutOffPoint() == null
        ))).thenAnswer(invocation -> invocation.getArgument(0));

        List<SchoolResponse> responses = schoolServiceImpl.getAllSchools();
        assertEquals(2, responses.size());
        
        SchoolResponse response = responses.get(0);
        assertEquals(dbSchool1.getId(), response.getId());
        assertEquals(apiSchool1.getName(), response.getName());
        assertEquals(apiSchool1.getLocation(), response.getLocation());
        assertEquals(apiSchool1.getAddress(), response.getAddress());
        assertEquals(apiSchool1.getPostalCode(), response.getPostalCode());
        assertEquals(apiSchool1.getWebsite(), response.getWebsite());
        assertEquals(apiSchool1.getEmail(), response.getEmail());
        assertEquals(apiSchool1.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(apiSchool1.getFaxNumber(), response.getFaxNumber());
        assertEquals(apiSchool1.getLevel(), response.getLevel());
        assertEquals(apiSchool1.getNatureCode(), response.getNatureCode());
        assertEquals(apiSchool1.getType(), response.getType());
        assertEquals(apiSchool1.getSessionCode(), response.getSessionCode());
        assertEquals(apiSchool1.getNearbyBusStation(), response.getNearbyBusStation());
        assertEquals(apiSchool1.getNearbyMrtStation(), response.getNearbyMrtStation());
        assertEquals(apiSchool1.getSubjects(), response.getSubjects());
        assertEquals(apiSchool1.getCcas(), response.getCcas());
        assertEquals(dbSchool1.getMinCutOffPoint(), response.getMinCutOffPoint());
        assertEquals(dbSchool1.getMaxCutOffPoint(), response.getMaxCutOffPoint());
    }
}
