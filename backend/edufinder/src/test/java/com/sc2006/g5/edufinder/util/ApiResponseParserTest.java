package com.sc2006.g5.edufinder.util;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc2006.g5.edufinder.dto.api.ApiLink;
import com.sc2006.g5.edufinder.dto.api.ApiResponse;
import com.sc2006.g5.edufinder.dto.api.ApiResult;
import com.sc2006.g5.edufinder.dto.api.CcaRecord;
import com.sc2006.g5.edufinder.dto.api.SchoolRecord;
import com.sc2006.g5.edufinder.dto.api.SubjectRecord;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ApiResponseParserTest {
    private ApiResponseParser apiResponseParser = new ApiResponseParser(new ObjectMapper());

    private static final String SCHOOL_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_688b934f82c1059ed0a6993d2a829089",
                "records": [
                {
                    "_id": 1,
                    "school_name": "ADMIRALTY PRIMARY SCHOOL",
                    "url_address": "https://admiraltypri.moe.edu.sg/",
                    "address": "11 WOODLANDS CIRCLE",
                    "postal_code": "738907",
                    "telephone_no": "63620598",
                    "fax_no": "63627512",
                    "email_address": "ADMIRALTY_PS@MOE.EDU.SG",
                    "mrt_desc": "Admiralty Station",
                    "bus_desc": "TIBS 965, 964, 913",
                    "dgp_code": "WOODLANDS",
                    "type_code": "GOVERNMENT SCHOOL",
                    "nature_code": "CO-ED SCHOOL",
                    "session_code": "FULL DAY",
                    "mainlevel_code": "PRIMARY"
                },
                {
                    "_id": 2,
                    "school_name": "ADMIRALTY SECONDARY SCHOOL",
                    "url_address": "http://www.admiraltysec.moe.edu.sg",
                    "address": "31 WOODLANDS CRESCENT",
                    "postal_code": "737916",
                    "telephone_no": "63651733",
                    "fax_no": "63652774",
                    "email_address": "Admiralty_SS@moe.edu.sg",
                    "mrt_desc": "ADMIRALTY MRT",
                    "bus_desc": "904",
                    "dgp_code": "WOODLANDS",
                    "zone_code": "NORTH",
                    "type_code": "GOVERNMENT SCHOOL",
                    "nature_code": "CO-ED SCHOOL",
                    "session_code": "SINGLE SESSION",
                    "mainlevel_code": "SECONDARY (S1-S5)"
                }
                ],
                "_links": {
                    "start": "/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089",
                    "next": "/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100"
                },
                "total": 337
            }
        }
    """;

    private static final String CCA_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_688b934f82c1059ed0a6993d2a829089",
                "records": [
                {
                    "_id": 1,
                    "School_name": "ADMIRALTY PRIMARY SCHOOL",
                    "school_section": "PRIMARY",
                    "cca_grouping_desc": "ART AND CRAFTS",
                    "cca_generic_name": "CLUBS AND SOCIETIES",
                    "cca_customized_name": "na"
                },
                {
                    "_id": 2,
                    "School_name": "ADMIRALTY PRIMARY SCHOOL",
                    "school_section": "PRIMARY",
                    "cca_grouping_desc": "CHINESE DANCE",
                    "cca_generic_name": "VISUAL AND PERFORMING ARTS",
                    "cca_customized_name": "na"
                },
                {
                    "_id": 3,
                    "School_name": "ADMIRALTY PRIMARY SCHOOL",
                    "school_section": "PRIMARY",
                    "cca_grouping_desc": "CHOIR",
                    "cca_generic_name": "VISUAL AND PERFORMING ARTS",
                    "cca_customized_name": "na"
                }
                ],
                "_links": {
                    "start": "/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089",
                    "next": "/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100"
                },
                "total": 337
            }
        }
    """;

    private static final String SUBJECT_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_f1d144e423570c9d84dbc5102c2e664d",
                "records": [
                {
                    "_id": 1,
                    "School_Name": "ADMIRALTY PRIMARY SCHOOL",
                    "Subject_Desc": "Art"
                },
                {
                    "_id": 2,
                    "School_Name": "ADMIRALTY PRIMARY SCHOOL",
                    "Subject_Desc": "Bengali Language"
                },
                {
                    "_id": 3,
                    "School_Name": "ADMIRALTY PRIMARY SCHOOL",
                    "Subject_Desc": "Chinese Language"
                }
                ],
                "_links": {
                    "start": "/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089",
                    "next": "/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100"
                },
                "total": 337
            }
        }
    """;

    @Test
    void parseSchoolRecord_test(){
        ApiResponse<SchoolRecord> response = apiResponseParser.parseApiRecord(SCHOOL_JSON, SchoolRecord.class);
        assertEquals(true, response.isSuccess());

        ApiResult<SchoolRecord> result = response.getResult();
        List<SchoolRecord> schoolRecords = result.getRecords();
        assertEquals(2, schoolRecords.size());

        SchoolRecord schoolRecord = schoolRecords.get(0);
        assertEquals("ADMIRALTY PRIMARY SCHOOL", schoolRecord.getName());
        assertEquals("https://admiraltypri.moe.edu.sg/", schoolRecord.getWebsite());
        assertEquals("11 WOODLANDS CIRCLE", schoolRecord.getAddress());
        assertEquals("738907", schoolRecord.getPostalCode());
        assertEquals("63620598", schoolRecord.getPhoneNumber());
        assertEquals("63627512", schoolRecord.getFaxNumber());
        assertEquals("ADMIRALTY_PS@MOE.EDU.SG", schoolRecord.getEmail());
        assertEquals("Admiralty Station", schoolRecord.getNearbyMrtStation());
        assertEquals("TIBS 965, 964, 913", schoolRecord.getNearbyBusStation());
        assertEquals("WOODLANDS", schoolRecord.getLocation());
        assertEquals("GOVERNMENT SCHOOL", schoolRecord.getType());
        assertEquals("CO-ED SCHOOL", schoolRecord.getNatureCode());
        assertEquals("FULL DAY", schoolRecord.getSessionCode());
        assertEquals("PRIMARY", schoolRecord.getLevel());

        ApiLink link = result.getLink();
        assertEquals("/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100", link.getNext());
        assertEquals(337, result.getTotal());
    }


    @Test
    void parseCcaRecord_test(){
        ApiResponse<CcaRecord> response = apiResponseParser.parseApiRecord(CCA_JSON, CcaRecord.class);
        assertEquals(true, response.isSuccess());

        ApiResult<CcaRecord> result = response.getResult();
        List<CcaRecord> ccaRecords = result.getRecords();
        assertEquals(3, ccaRecords.size());

        CcaRecord ccaRecord = ccaRecords.get(0);
        assertEquals("ADMIRALTY PRIMARY SCHOOL", ccaRecord.getSchoolName());
        assertEquals("ART AND CRAFTS", ccaRecord.getCca());

        ApiLink link = result.getLink();
        assertEquals("/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100", link.getNext());
        assertEquals(337, result.getTotal());
    }

    @Test
    void parseSubjectRecord_test(){
        ApiResponse<SubjectRecord> response = apiResponseParser.parseApiRecord(SUBJECT_JSON, SubjectRecord.class);
        assertEquals(true, response.isSuccess());

        ApiResult<SubjectRecord> result = response.getResult();
        List<SubjectRecord> subjectRecords = result.getRecords();
        assertEquals(3, subjectRecords.size());

        SubjectRecord schoolRecord = subjectRecords.get(0);
        assertEquals("ADMIRALTY PRIMARY SCHOOL", schoolRecord.getSchoolName());
        assertEquals("Art", schoolRecord.getSubject());

        ApiLink link = result.getLink();
        assertEquals("/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100", link.getNext());
        assertEquals(337, result.getTotal());
    }
}
