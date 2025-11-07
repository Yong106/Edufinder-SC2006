package com.sc2006.g5.edufinder.unit.util;

import com.sc2006.g5.edufinder.dto.api.*;
import com.sc2006.g5.edufinder.util.ApiResponseParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseParserTest {
    private final ApiResponseParser apiResponseParser = new ApiResponseParser(new ObjectMapper());

    @Nested
    @DisplayName("parse school record")
    class ParseSchoolRecordTest {
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
                        "telephone_no_2": "na",
                        "fax_no": "63627512",
                        "fax_no_2": "na",
                        "email_address": "ADMIRALTY_PS@MOE.EDU.SG",
                        "mrt_desc": "Admiralty Station",
                        "bus_desc": "TIBS 965, 964, 913",
                        "principal_name": "MR CHEN ZHONGYI",
                        "first_vp_name": "MADAM CHUA MUI LING",
                        "second_vp_name": "MR HAMRI BIN A JALIL",
                        "third_vp_name": "na",
                        "fourth_vp_name": "na",
                        "fifth_vp_name": "na",
                        "sixth_vp_name": "na",
                        "dgp_code": "WOODLANDS",
                        "zone_code": "NORTH",
                        "type_code": "GOVERNMENT SCHOOL",
                        "nature_code": "CO-ED SCHOOL",
                        "session_code": "FULL DAY",
                        "mainlevel_code": "PRIMARY",
                        "sap_ind": "No",
                        "autonomous_ind": "Yes",
                        "gifted_ind": "No",
                        "ip_ind": "No",
                        "mothertongue1_code": "CHINESE",
                        "mothertongue2_code": null,
                        "mothertongue3_code": "TAMIL"
                   },
                   {
                        "_id": 2,
                        "school_name": "ADMIRALTY SECONDARY SCHOOL",
                        "url_address": "http://www.admiraltysec.moe.edu.sg",
                        "address": "31 WOODLANDS CRESCENT   ",
                        "postal_code": "737916",
                        "telephone_no": "63651733",
                        "telephone_no_2": "63654596",
                        "fax_no": "63652774",
                        "fax_no_2": "na",
                        "email_address": "Admiralty_SS@moe.edu.sg",
                        "mrt_desc": "ADMIRALTY MRT",
                        "bus_desc": "904",
                        "principal_name": "MR TAN KELLY",
                        "first_vp_name": "MR CHAN YEW REN EUGENE",
                        "second_vp_name": "MR TAN HOCK CHUAN MICHAEL",
                        "third_vp_name": "na",
                        "fourth_vp_name": "na",
                        "fifth_vp_name": "na",
                        "sixth_vp_name": "na",
                        "dgp_code": "WOODLANDS",
                        "zone_code": "NORTH",
                        "type_code": "GOVERNMENT SCHOOL",
                        "nature_code": "CO-ED SCHOOL",
                        "session_code": "SINGLE SESSION",
                        "mainlevel_code": "SECONDARY (S1-S5)",
                        "sap_ind": "No",
                        "autonomous_ind": "No",
                        "gifted_ind": "No",
                        "ip_ind": "No",
                        "mothertongue1_code": "CHINESE",
                        "mothertongue2_code": "MALAY",
                        "mothertongue3_code": "TAMIL"
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
        @DisplayName("should return api response with school record")
        void parseSchoolRecord_test(){
            ApiResponse<SchoolRecord> response = apiResponseParser.parseApiRecord(SCHOOL_JSON, SchoolRecord.class);
            assertTrue(response.isSuccess());

            ApiResult<SchoolRecord> result = response.getResult();
            List<SchoolRecord> schoolRecords = result.getRecords();
            assertEquals(2, schoolRecords.size());

            SchoolRecord schoolRecord = schoolRecords.getFirst();
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

            assertEquals("CHINESE", schoolRecord.getMotherTongue1());
            assertNull(schoolRecord.getMotherTongue2());
            assertEquals("TAMIL", schoolRecord.getMotherTongue3());

            assertFalse(schoolRecord.isSapInd());
            assertTrue(schoolRecord.isAutonomousInd());
            assertFalse(schoolRecord.isGiftedInd());
            assertFalse(schoolRecord.isIpInd());

            ApiLink link = result.getLink();
            assertEquals("/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100", link.getNext());
            assertEquals(337, result.getTotal());
        }
    }

    @Nested
    @DisplayName("parse cca record")
    class ParseCcaRecordTest {
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

        @Test
        @DisplayName("should return api response with cca record")
        void shouldReturnApiResponseWithCcaRecord() {
            ApiResponse<CcaRecord> response = apiResponseParser.parseApiRecord(CCA_JSON, CcaRecord.class);
            assertTrue(response.isSuccess());

            ApiResult<CcaRecord> result = response.getResult();
            List<CcaRecord> ccaRecords = result.getRecords();
            assertEquals(3, ccaRecords.size());

            CcaRecord ccaRecord = ccaRecords.getFirst();
            assertEquals("ADMIRALTY PRIMARY SCHOOL", ccaRecord.getSchoolName());
            assertEquals("ART AND CRAFTS", ccaRecord.getCca());
            assertEquals("CLUBS AND SOCIETIES", ccaRecord.getType());

            ApiLink link = result.getLink();
            assertEquals("/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100", link.getNext());
            assertEquals(337, result.getTotal());
        }
    }

    @Nested
    @DisplayName("parse subject record")
    class ParseSubjectRecordTest {
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
        @DisplayName("should return api response with subject record")
        void shouldReturnApiResponseWithSubjectRecord() {
            ApiResponse<SubjectRecord> response = apiResponseParser.parseApiRecord(SUBJECT_JSON, SubjectRecord.class);
            assertTrue(response.isSuccess());

            ApiResult<SubjectRecord> result = response.getResult();
            List<SubjectRecord> subjectRecords = result.getRecords();
            assertEquals(3, subjectRecords.size());

            SubjectRecord schoolRecord = subjectRecords.getFirst();
            assertEquals("ADMIRALTY PRIMARY SCHOOL", schoolRecord.getSchoolName());
            assertEquals("Art", schoolRecord.getSubject());

            ApiLink link = result.getLink();
            assertEquals("/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100", link.getNext());
            assertEquals(337, result.getTotal());
        }
    }

    @Nested
    @DisplayName("parse programme record")
    class ParseProgrammeRecordTest {
        private static final String PROGRAMME_JSON = """
            {
                "success": true,
                "result": {
                    "resource_id": "d_f1d144e423570c9d84dbc5102c2e664d",
                    "records": [
                    {
                     "_id": 1,
                     "school_name": "HWA CHONG INSTITUTION",
                     "moe_programme_desc": "Art Elective Programme "
                    },
                    {
                     "_id": 2,
                     "school_name": "NANYANG JUNIOR COLLEGE",
                     "moe_programme_desc": "Art Elective Programme"
                    },
                    {
                     "_id": 3,
                     "school_name": "NATIONAL JUNIOR COLLEGE",
                     "moe_programme_desc": "Art Elective Programme "
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
        @DisplayName("should return api response with programme record")
        void shouldReturnApiResponseWithProgrammeRecord() {
            ApiResponse<ProgrammeRecord> response = apiResponseParser.parseApiRecord(PROGRAMME_JSON, ProgrammeRecord.class);
            assertTrue(response.isSuccess());

            ApiResult<ProgrammeRecord> result = response.getResult();
            List<ProgrammeRecord> programmeRecords = result.getRecords();
            assertEquals(3, programmeRecords.size());

            ProgrammeRecord programmeRecord = programmeRecords.getFirst();
            assertEquals("HWA CHONG INSTITUTION", programmeRecord.getSchoolName());
            assertEquals("Art Elective Programme ", programmeRecord.getProgramme());

            ApiLink link = result.getLink();
            assertEquals("/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100", link.getNext());
            assertEquals(337, result.getTotal());
        }

    }
}
