package com.sc2006.g5.edufinder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sc2006.g5.edufinder.dto.response.SchoolResponse;
import com.sc2006.g5.edufinder.model.ApiSchool;
import com.sc2006.g5.edufinder.model.DbSchool;
import com.sc2006.g5.edufinder.repository.ApiSchoolRepository;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;

@Service
public class SchoolServiceImpl implements SchoolService {
    private final ApiSchoolRepository apiSchoolRepository;
    private final DbSchoolRepository dbSchoolRepository;

    @Autowired
    public SchoolServiceImpl(ApiSchoolRepository apiSchoolRepository, DbSchoolRepository dbSchoolRepository){
        this.apiSchoolRepository = apiSchoolRepository;
        this.dbSchoolRepository = dbSchoolRepository;
    }

    @Override
    public List<SchoolResponse> getAllSchools() {
        List<SchoolResponse> responses = new ArrayList<>();
        List<ApiSchool> apiSchools = apiSchoolRepository.getApiSchools();

        for(ApiSchool apiSchool : apiSchools){
            String name = apiSchool.getName();
            List<DbSchool> dbSchools = dbSchoolRepository.findByName(name);
            DbSchool dbSchool = null;

            if(dbSchools.size() == 0){
                dbSchool = DbSchool.builder()
                    .name(name)
                    .build();
                dbSchoolRepository.save(dbSchool);
            }
            else{
                dbSchool = dbSchools.get(0);
            }

            SchoolResponse response = SchoolResponse.builder()
                .id(dbSchool.getId())
                .name(name)
                .location(apiSchool.getLocation())
				.ccas(apiSchool.getCcas())
				.subjects(apiSchool.getSubjects())
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
				.build();

            responses.add(response);
        }

        return responses;
    }

    
}
