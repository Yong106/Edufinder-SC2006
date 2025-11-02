package com.sc2006.g5.edufinder.repository;

import java.util.*;

import com.sc2006.g5.edufinder.dto.api.*;
import com.sc2006.g5.edufinder.mapper.SchoolMapper;
import com.sc2006.g5.edufinder.model.school.Cca;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.sc2006.g5.edufinder.model.school.ApiSchool;
import com.sc2006.g5.edufinder.service.ApiClientService;
import com.sc2006.g5.edufinder.util.ApiResponseParser;

@Repository
@RequiredArgsConstructor
public class ApiSchoolRepositoryImpl implements ApiSchoolRepository {

	private List<ApiSchool> apiSchools;

	private final ApiClientService apiClientService;
	private final ApiResponseParser apiResponseParser;
    private final SchoolMapper schoolMapper;

	public static final String API_DOMAIN = "https://data.gov.sg/";
	public static final String API_ENDPOINT = "api/action/datastore_search?resource_id=";

	public static final String GENERAL_INFORMATION_DATASET_ID = "d_688b934f82c1059ed0a6993d2a829089";
	public static final String SUBJECTS_DATASET_ID = "d_f1d144e423570c9d84dbc5102c2e664d";
	public static final String CCAS_DATASET_ID = "d_9aba12b5527843afb0b2e8e4ed6ac6bd";
    public static final String MOE_PROGRAMMES_DATASET_ID = "d_b0697d22a7837a4eddf72efb66a36fc2";

//    @PostConstruct
//    public void init() {
//        refreshApiSchools();
//    }

    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
    public void refreshApiSchools() {
        List<SchoolRecord> schoolRecords = getSchoolGeneralInformation();
        Map<String, List<String>> subjectMap = getSchoolSubjects();
        Map<String, List<Cca>> ccaMap = getSchoolCcas();
        Map<String, List<String>> programmeMap = getSchoolProgrammes();

        apiSchools = schoolMapper.toApiSchools(schoolRecords, subjectMap, ccaMap, programmeMap);
    }

	public List<ApiSchool> getApiSchools() {
		if(apiSchools == null) {
            refreshApiSchools();
        }

		return apiSchools;
	}

	private <R extends ApiRecord> List<R> getAllApiRecords(String firstApiEndpoint, Class<R> apiRecordClass){
		List<R> records = new ArrayList<>();
		String nextApiEndpoint = firstApiEndpoint;
		int total = 1; 

		while(records.size() < total){
			String json = apiClientService.get(API_DOMAIN + nextApiEndpoint, null);
			ApiResponse<R> response = apiResponseParser.parseApiRecord(json, apiRecordClass);
			ApiResult<R> result = response.getResult();

			List<R> nextRecords = result.getRecords();
			records.addAll(nextRecords);

			nextApiEndpoint = result.getLink().getNext();
			total = result.getTotal();
		}

		return records;
	}

	private List<SchoolRecord> getSchoolGeneralInformation() {
		String firstApiEndpoint = API_ENDPOINT + GENERAL_INFORMATION_DATASET_ID;
		return getAllApiRecords(firstApiEndpoint , SchoolRecord.class);
	}

	private Map<String, List<String>> getSchoolSubjects() {
		String firstApiEndpoint = API_ENDPOINT + SUBJECTS_DATASET_ID;
		List<SubjectRecord> subjectRecords = getAllApiRecords(firstApiEndpoint, SubjectRecord.class);
		
		Map<String, List<String>> subjectMap = new HashMap<>();
		for(SubjectRecord subjectRecord : subjectRecords){
			String schoolName = subjectRecord.getSchoolName();

			if(!subjectMap.containsKey(schoolName)){
				subjectMap.put(schoolName, new ArrayList<>());
			}

			subjectMap.get(schoolName).add(subjectRecord.getSubject());
		}

		return subjectMap;
	}

	private Map<String, List<Cca>> getSchoolCcas() {
		String firstApiEndpoint = API_ENDPOINT + CCAS_DATASET_ID;
		List<CcaRecord> ccaRecords = getAllApiRecords(firstApiEndpoint, CcaRecord.class);
		
		Map<String, List<Cca>> ccaMap = new HashMap<>();
		for(CcaRecord ccaRecord : ccaRecords){
			String schoolName = ccaRecord.getSchoolName();

			if(!ccaMap.containsKey(schoolName)){
				ccaMap.put(schoolName, new ArrayList<>());
			}

            Cca cca = Cca.builder()
                .name(ccaRecord.getCca())
                .type(ccaRecord.getType())
                .build();

			ccaMap.get(schoolName).add(cca);
		}

		return ccaMap;
	}

    private Map<String, List<String>> getSchoolProgrammes() {
        String firstApiEndpoint = API_ENDPOINT + MOE_PROGRAMMES_DATASET_ID;
        List<ProgrammeRecord> programmeRecords = getAllApiRecords(firstApiEndpoint, ProgrammeRecord.class);

        Map<String, Set<String>> programmesMap = new HashMap<>();
        for(ProgrammeRecord programmeRecord : programmeRecords){
            String schoolName = programmeRecord.getSchoolName();

            if(!programmesMap.containsKey(schoolName)){
                programmesMap.put(schoolName, new HashSet<>());
            }

            programmesMap.get(schoolName).add(programmeRecord.getProgramme());
        }

        // The dataset contains duplicated record
        Map<String, List<String>> deduplicatedProgrammesMap = new HashMap<>();
        programmesMap.forEach((schoolName, programmes) ->
           deduplicatedProgrammesMap.put(schoolName, programmes.stream().toList())
        );

        return deduplicatedProgrammesMap;
    }

}