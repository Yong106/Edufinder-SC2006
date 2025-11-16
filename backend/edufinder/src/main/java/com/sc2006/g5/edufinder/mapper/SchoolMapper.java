package com.sc2006.g5.edufinder.mapper;

import com.sc2006.g5.edufinder.dto.api.SchoolRecord;
import com.sc2006.g5.edufinder.model.school.Cca;
import org.springframework.stereotype.Component;

import com.sc2006.g5.edufinder.dto.response.SchoolResponse;
import com.sc2006.g5.edufinder.model.school.ApiSchool;
import com.sc2006.g5.edufinder.model.school.DbSchool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapper for school-related model.
 * <p>
 * Provides method to map to {@link SchoolResponse} DTOs and {@link ApiSchool} entity.
 */
@Component
public class SchoolMapper {

    /**
     * Converts {@link ApiSchool} and {@link DbSchool} into a {@link SchoolResponse}.
     *
     * @param apiSchool the {@code ApiSchool} to map
     * @param dbSchool the {@code DbSchool} to map
     *
     * @return the mapped {@code SchoolResponse}
     *
     * @see ApiSchool
     * @see DbSchool
     * @see SchoolResponse
     */
    public SchoolResponse toSchoolResponse(ApiSchool apiSchool, DbSchool dbSchool){
        return SchoolResponse.builder()
            .id(dbSchool.getId())
            .name(apiSchool.getName())
            .location(apiSchool.getLocation())
            .ccas(apiSchool.getCcas())
            .subjects(apiSchool.getSubjects())
            .programmes(apiSchool.getProgrammes())
            .level(apiSchool.getLevel())
            .natureCode(apiSchool.getNatureCode())
            .type(apiSchool.getType())
            .sessionCode(apiSchool.getSessionCode())
            .address(apiSchool.getAddress())
            .postalCode(apiSchool.getPostalCode())
            .nearbyBusStation(apiSchool.getNearbyBusStation())
            .nearbyMrtStation(apiSchool.getNearbyMrtStation())
            .website(apiSchool.getWebsite())
            .email(apiSchool.getEmail())
            .phoneNumber(apiSchool.getPhoneNumber())
            .faxNumber(apiSchool.getFaxNumber())
            .minCutOffPoint(dbSchool.getMinCutOffPoint())
            .maxCutOffPoint(dbSchool.getMaxCutOffPoint())
            .motherTongue1(apiSchool.getMotherTongue1())
            .motherTongue2(apiSchool.getMotherTongue2())
            .motherTongue3(apiSchool.getMotherTongue3())
            .sapInd(apiSchool.isSapInd())
            .autonomousInd(apiSchool.isAutonomousInd())
            .giftedInd(apiSchool.isGiftedInd())
            .ipInd(apiSchool.isIpInd())
            .build();
    }

    /**
     * Converts a list of {@link SchoolRecord} into list of {@link ApiSchool}.
     *
     * @param schoolRecords the list of {@code SchoolRecord} to map
     * @param subjectMap the map containing school name as the key and its list of subjects as value
     * @param ccaMap the map containing school name as the key and its list of {@code Cca} as value
     * @param programmeMap the map containing school name as the key and its list of programme as value
     *
     * @return a {@code List} containing the mapped {@code ApiSchool}
     *
     * @see ApiSchool
     * @see SchoolRecord
     * @see Cca
     */
    public List<ApiSchool> toApiSchools(
            List<SchoolRecord> schoolRecords,
            Map<String, List<String>> subjectMap,
            Map<String, List<Cca>> ccaMap,
            Map<String, List<String>> programmeMap) {

        List<ApiSchool> apiSchools = new ArrayList<>();

        for(SchoolRecord schoolRecord : schoolRecords){
            String schoolName = schoolRecord.getName();
            List<String> subjects = subjectMap.get(schoolName);
            List<Cca> ccas = ccaMap.get(schoolName);
            List<String> programmes = programmeMap.get(schoolName);

            ApiSchool school = ApiSchool.builder()
                    .name(schoolName)
                    .location(schoolRecord.getLocation())
                    .ccas(ccas == null ? List.of() : ccas)
                    .subjects(subjects == null ? List.of() : subjects)
                    .programmes(programmes ==  null ? List.of() : programmes)
                    .level(schoolRecord.getLevel())
                    .natureCode(schoolRecord.getNatureCode())
                    .type(schoolRecord.getType())
                    .sessionCode(schoolRecord.getSessionCode())
                    .address(schoolRecord.getAddress())
                    .postalCode(schoolRecord.getPostalCode())
                    .nearbyBusStation(schoolRecord.getNearbyBusStation())
                    .nearbyMrtStation(schoolRecord.getNearbyMrtStation())
                    .website(schoolRecord.getWebsite())
                    .email(schoolRecord.getEmail())
                    .phoneNumber(schoolRecord.getPhoneNumber())
                    .faxNumber(schoolRecord.getFaxNumber())
                    .motherTongue1(schoolRecord.getMotherTongue1())
                    .motherTongue2(schoolRecord.getMotherTongue2())
                    .motherTongue3(schoolRecord.getMotherTongue3())
                    .sapInd(schoolRecord.isSapInd())
                    .autonomousInd(schoolRecord.isAutonomousInd())
                    .giftedInd(schoolRecord.isGiftedInd())
                    .ipInd(schoolRecord.isIpInd())
                    .build();

            apiSchools.add(school);
        }

        return apiSchools;
    }
}
