package com.sc2006.g5.edufinder.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sc2006.g5.edufinder.dto.api.ApiRecord;
import com.sc2006.g5.edufinder.dto.api.ApiResponse;
import com.sc2006.g5.edufinder.dto.api.ApiResult;
import com.sc2006.g5.edufinder.dto.api.CcaRecord;
import com.sc2006.g5.edufinder.dto.api.SchoolRecord;
import com.sc2006.g5.edufinder.dto.api.SubjectRecord;
import com.sc2006.g5.edufinder.model.ApiSchool;
import com.sc2006.g5.edufinder.service.ApiClientService;
import com.sc2006.g5.edufinder.util.ApiResponseParser;

@Repository
public class ApiSchoolRepositoryImpl implements ApiSchoolRepository {

	private List<ApiSchool> apiSchools;
	private final ApiClientService apiClientService;
	private final ApiResponseParser apiResponseParser;

	public static final String apiDomain = "https://data.gov.sg/";
	public static final String apiEndpoint = "api/action/datastore_search?resource_id=";
	public static final String generalInformationDatasetId = "d_688b934f82c1059ed0a6993d2a829089";
	public static final String subjectsDatasetId = "d_f1d144e423570c9d84dbc5102c2e664d";
	public static final String ccasDatasetId = "d_9aba12b5527843afb0b2e8e4ed6ac6bd";

	@Autowired
	public ApiSchoolRepositoryImpl(ApiClientService apiClientService, ApiResponseParser apiResponseParser){
		this.apiClientService = apiClientService;
		this.apiResponseParser = apiResponseParser;
	}

	public List<ApiSchool> getApiSchools() {
		if(apiSchools != null){
			return apiSchools;
		}

		apiSchools = new ArrayList<>();

		List<SchoolRecord> schoolRecords = getSchoolGeneralInformation();
		Map<String, List<String>> subjectMap = getSchoolSubjects();
		Map<String, List<String>> ccaMap = getSchoolCcas();

		for(SchoolRecord schoolRecord : schoolRecords){
			String schoolName = schoolRecord.getName();
			List<String> subjects = subjectMap.get(schoolName);
			List<String> ccas = ccaMap.get(schoolName);

			ApiSchool school = ApiSchool.builder()
				.name(schoolName)
				.location(schoolRecord.getLocation())
				.ccas(ccas)
				.subjects(subjects)
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
				.build();

			apiSchools.add(school);
		}

		return apiSchools;
	}

	private <R extends ApiRecord> List<R> getAllApiRecords(String firstApiEndpoint, Class<R> apiRecordClass){
		List<R> records = new ArrayList<>();
		String nextApiEndpoint = firstApiEndpoint;
		int total = 1; 

		while(records.size() < total){
			String json = apiClientService.get(apiDomain + nextApiEndpoint, null);
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
		String firstApiEndpoint = apiEndpoint + generalInformationDatasetId;
		return getAllApiRecords(firstApiEndpoint , SchoolRecord.class);
	}

	private Map<String, List<String>> getSchoolSubjects() {
		String firstApiEndpoint = apiEndpoint + subjectsDatasetId;
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

	private Map<String, List<String>> getSchoolCcas() {
		String firstApiEndpoint = apiEndpoint + ccasDatasetId;
		List<CcaRecord> ccaRecords = getAllApiRecords(firstApiEndpoint, CcaRecord.class);
		
		Map<String, List<String>> ccaMap = new HashMap<>();
		for(CcaRecord ccaRecord : ccaRecords){
			String schoolName = ccaRecord.getSchoolName();

			if(!ccaMap.containsKey(schoolName)){
				ccaMap.put(schoolName, new ArrayList<>());
			}

			ccaMap.get(schoolName).add(ccaRecord.getCca());
		}

		return ccaMap;
	}

}