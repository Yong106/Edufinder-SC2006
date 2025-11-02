package com.sc2006.g5.edufinder.service;

import java.util.ArrayList;
import java.util.List;

import com.sc2006.g5.edufinder.dto.response.SchoolsResponse;
import com.sc2006.g5.edufinder.mapper.SchoolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.sc2006.g5.edufinder.dto.response.SchoolResponse;
import com.sc2006.g5.edufinder.model.school.ApiSchool;
import com.sc2006.g5.edufinder.model.school.DbSchool;
import com.sc2006.g5.edufinder.repository.ApiSchoolRepository;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final ApiSchoolRepository apiSchoolRepository;
    private final DbSchoolRepository dbSchoolRepository;
    private final SchoolMapper schoolMapper;

    @Override
    public SchoolsResponse getAllSchools() {
        List<SchoolResponse> schools = new ArrayList<>();
        List<ApiSchool> apiSchools = apiSchoolRepository.getApiSchools();

        for(ApiSchool apiSchool : apiSchools){
            String name = apiSchool.getName();
            DbSchool dbSchool = dbSchoolRepository.findOneByName(name)
                    .orElse(null);

            if(dbSchool == null){
                dbSchool = DbSchool.builder()
                        .name(name)
                        .build();
                dbSchoolRepository.save(dbSchool);
            }

            schools.add(schoolMapper.toSchoolResponse(apiSchool, dbSchool));
        }

        return SchoolsResponse.builder()
                .schools(schools)
                .build();
    }
    
}
