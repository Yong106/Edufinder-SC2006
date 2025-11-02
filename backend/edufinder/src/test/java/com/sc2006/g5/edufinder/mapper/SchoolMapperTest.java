package com.sc2006.g5.edufinder.mapper;

import com.sc2006.g5.edufinder.dto.api.SchoolRecord;
import com.sc2006.g5.edufinder.model.school.ApiSchool;
import com.sc2006.g5.edufinder.model.school.Cca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchoolMapperTest {

    SchoolMapper schoolMapper = new SchoolMapper();

    private static final String SCHOOL_NAME_1 = "school1";
    private static final String SCHOOL_NAME_2 = "school2";

    @Nested
    @DisplayName("toApiSchools")
    class toApiSchoolsTest{

        @Test
        @DisplayName("should return api schools")
        void shouldReturnApiSchools() {

            SchoolRecord schoolRecord1 = SchoolRecord.builder()
                .name(SCHOOL_NAME_1)
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
                .motherTongue1("mother1")
                .motherTongue2("mother2")
                .motherTongue3("mother3")
                .sapInd(true)
                .autonomousInd(false)
                .giftedInd(false)
                .ipInd(true)
                .build();

            SchoolRecord schoolRecord2 = SchoolRecord.builder()
                .name(SCHOOL_NAME_2)
                .build();

            List<SchoolRecord> schoolRecords = List.of(schoolRecord1, schoolRecord2);

            List<String> subjects = List.of("subject1", "subject2");
            Map<String, List<String>> subjectMap = Map.of(
                SCHOOL_NAME_1, subjects
            );

            Cca cca = Cca.builder()
                .name("cca")
                .build();

            List<Cca> ccas = List.of(cca);
            Map<String, List<Cca>> ccaMap = Map.of(
                SCHOOL_NAME_1, ccas
            );

            List<String> programmes = List.of("prog1", "prog2");
            Map<String, List<String>> programmeMap = Map.of(
                SCHOOL_NAME_1, programmes
            );

            List<ApiSchool> schools = schoolMapper.toApiSchools(schoolRecords, subjectMap, ccaMap, programmeMap);
            assertEquals(2, schools.size());

            ApiSchool school1 = schools.get(0);
            assertEquals(SCHOOL_NAME_1, school1.getName());
            assertEquals(subjects, school1.getSubjects());
            assertEquals(ccas, school1.getCcas());
            assertEquals(programmes, school1.getProgrammes());

            assertEquals(schoolRecord1.getLocation(), school1.getLocation());
            assertEquals(schoolRecord1.getAddress(), school1.getAddress());
            assertEquals(schoolRecord1.getPostalCode(), school1.getPostalCode());
            assertEquals(schoolRecord1.getWebsite(), school1.getWebsite());
            assertEquals(schoolRecord1.getEmail(), school1.getEmail());
            assertEquals(schoolRecord1.getPhoneNumber(), school1.getPhoneNumber());
            assertEquals(schoolRecord1.getFaxNumber(), school1.getFaxNumber());
            assertEquals(schoolRecord1.getLevel(), school1.getLevel());
            assertEquals(schoolRecord1.getNatureCode(), school1.getNatureCode());
            assertEquals(schoolRecord1.getType(), school1.getType());
            assertEquals(schoolRecord1.getSessionCode(), school1.getSessionCode());
            assertEquals(schoolRecord1.getNearbyBusStation(), school1.getNearbyBusStation());
            assertEquals(schoolRecord1.getNearbyMrtStation(), school1.getNearbyMrtStation());
            assertEquals(schoolRecord1.getMotherTongue1(), school1.getMotherTongue1());
            assertEquals(schoolRecord1.getMotherTongue2(), school1.getMotherTongue2());
            assertEquals(schoolRecord1.getMotherTongue3(), school1.getMotherTongue3());
            assertEquals(schoolRecord1.isSapInd(), school1.isSapInd());
            assertEquals(schoolRecord1.isAutonomousInd(), school1.isAutonomousInd());
            assertEquals(schoolRecord1.isGiftedInd(), school1.isGiftedInd());
            assertEquals(schoolRecord1.isIpInd(), school1.isIpInd());

            ApiSchool school2 = schools.get(1);
            assertEquals(SCHOOL_NAME_2, school2.getName());
            assertEquals(List.of(), school2.getSubjects());
            assertEquals(List.of(), school2.getCcas());
            assertEquals(List.of(), school2.getProgrammes());

        }

    }
}
