package com.sc2006.g5.edufinder.repository;

import java.util.*;

import com.sc2006.g5.edufinder.dto.api.*;
import com.sc2006.g5.edufinder.mapper.SchoolMapper;
import com.sc2006.g5.edufinder.model.school.Cca;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.sc2006.g5.edufinder.model.school.ApiSchool;
import com.sc2006.g5.edufinder.service.ApiClientService;
import com.sc2006.g5.edufinder.util.ApiResponseParser;

@Repository
@RequiredArgsConstructor
public class ApiSchoolRepositoryImpl implements ApiSchoolRepository {

	@Getter
    private List<ApiSchool> apiSchools;

	private final ApiClientService apiClientService;
	private final ApiResponseParser apiResponseParser;
    private final SchoolMapper schoolMapper;
    private final Environment env;

	public static final String API_DOMAIN = "https://data.gov.sg/";
	public static final String API_ENDPOINT = "api/action/datastore_search?resource_id=";

	public static final String GENERAL_INFORMATION_DATASET_ID = "d_688b934f82c1059ed0a6993d2a829089";
	public static final String SUBJECTS_DATASET_ID = "d_f1d144e423570c9d84dbc5102c2e664d";
	public static final String CCAS_DATASET_ID = "d_9aba12b5527843afb0b2e8e4ed6ac6bd";
    public static final String MOE_PROGRAMMES_DATASET_ID = "d_b0697d22a7837a4eddf72efb66a36fc2";

    private static final Logger logger = LoggerFactory.getLogger(ApiSchoolRepositoryImpl.class);

    @PostConstruct
    public void init() {
        if(Arrays.asList(env.getActiveProfiles()).contains("test")) return;
        refreshApiSchools();
    }

    @Scheduled(fixedRateString = "${app.school.refresh-time}")
    public void refreshApiSchools() {
        logger.info("Refreshing API schools");

        logger.info("Fetching school general information data");
        List<SchoolRecord> schoolRecords = getSchoolGeneralInformation();

        logger.info("Fetching school subject data");
        Map<String, List<String>> subjectMap = getSchoolSubjects();

        logger.info("Fetching school cca data");
        Map<String, List<Cca>> ccaMap = getSchoolCcas();

        logger.info("Fetching school programme data");
        Map<String, List<String>> programmeMap = getSchoolProgrammes();

        apiSchools = schoolMapper.toApiSchools(schoolRecords, subjectMap, ccaMap, programmeMap);

        logger.info("API schools refreshed");
    }

    /**
     * Fetch all record of a specific dataset from the <a href="https://data.gov.sg/">data.gov.sg</a> API.
     * @param firstApiEndpoint the path of API after <a href="https://data.gov.sg/">https://data.gov.sg/</a>;
     * @param apiRecordClass the class of {@code ApiRecord} to fetch
     *
     * @return {@code list} of {@code ApiRecord}
     * @param <R> the type of {@code ApiRecord} to fetch
     *
     * @see ApiRecord
     */
    private <R extends ApiRecord> List<R> getAllApiRecords(String firstApiEndpoint, Class<R> apiRecordClass){
		List<R> records = new ArrayList<>();
		String nextApiEndpoint = firstApiEndpoint;
		int total = 1; // Initial value to fetch the first time

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

    /**
     * Get all {@link SchoolRecord} from <a href="https://data.gov.sg/datasets?agencies=Ministry+of+Education+(MOE)&resultId=d_688b934f82c1059ed0a6993d2a829089">General information of schools</a>.
     * @return all {@code SchoolRecord} fetched from the API
     */
	private List<SchoolRecord> getSchoolGeneralInformation() {
		String firstApiEndpoint = API_ENDPOINT + GENERAL_INFORMATION_DATASET_ID;
		return getAllApiRecords(firstApiEndpoint , SchoolRecord.class);
	}

    /**
     * Get all schools' subjects from <a href="https://data.gov.sg/datasets?agencies=Ministry+of+Education+(MOE)&resultId=d_f1d144e423570c9d84dbc5102c2e664d">Subjects Offered</a>
     * @return a map containing school name as the key and its list of subjects as value
     */
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

    /**
     * Get all schools' CCAs from <a href="https://data.gov.sg/datasets?agencies=Ministry+of+Education+(MOE)&resultId=d_9aba12b5527843afb0b2e8e4ed6ac6bd">Co-curricular activities (CCAs)</a>
     * @return a map containing school name as the key and its list of {@link Cca} as value
     */
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

    /**
     * Get all schools' MOE programmes from <a href="https://data.gov.sg/datasets?agencies=Ministry+of+Education+(MOE)&resultId=d_b0697d22a7837a4eddf72efb66a36fc2">MOE Programmes</a>
     * @return a map containing school name as the key and its list of programmes as value
     */
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