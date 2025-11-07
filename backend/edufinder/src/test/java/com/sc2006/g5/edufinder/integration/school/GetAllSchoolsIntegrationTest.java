package com.sc2006.g5.edufinder.integration.school;

import com.sc2006.g5.edufinder.repository.DbSchoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@ActiveProfiles("test")
class GetAllSchoolsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DbSchoolRepository dbSchoolRepository;

    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    private static final String SCHOOL_GENERAL_INFORMATION_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089";
    private static final String NEXT_SCHOOL_GENERAL_INFORMATION_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100";

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
               }
               ],
               "_links": {
                   "start": "/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089",
                   "next": "/api/action/datastore_search?resource_id=d_688b934f82c1059ed0a6993d2a829089&offset=100"
               },
               "total": 2
            }
        }
    """;

    private static final String NEXT_SCHOOL_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_688b934f82c1059ed0a6993d2a829089",
                "records": [
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
               "total": 2
            }
        }
    """;

    private static final String SUBJECT_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=d_f1d144e423570c9d84dbc5102c2e664d";
    private static final String NEXT_SUBJECT_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=d_f1d144e423570c9d84dbc5102c2e664d&offset=100";

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
                }
                ],
                "_links": {
                    "start": "/api/action/datastore_search?resource_id=d_f1d144e423570c9d84dbc5102c2e664d",
                    "next": "/api/action/datastore_search?resource_id=d_f1d144e423570c9d84dbc5102c2e664d&offset=100"
                },
                "total": 3
            }
        }
    """;

    private static final String NEXT_SUBJECT_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_f1d144e423570c9d84dbc5102c2e664d",
                "records": [
                {
                    "_id": 3,
                    "School_Name": "ADMIRALTY PRIMARY SCHOOL",
                    "Subject_Desc": "Chinese Language"
                }
                ],
                "_links": {
                    "start": "/api/action/datastore_search?resource_id=d_f1d144e423570c9d84dbc5102c2e664d",
                    "next": "/api/action/datastore_search?resource_id=d_f1d144e423570c9d84dbc5102c2e664d&offset=100"
                },
                "total": 3
            }
        }
    """;

    private static final String CCA_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=d_9aba12b5527843afb0b2e8e4ed6ac6bd";
    private static final String NEXT_CCA_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=d_9aba12b5527843afb0b2e8e4ed6ac6bd&offset=100";

    private static final String CCA_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_9aba12b5527843afb0b2e8e4ed6ac6bd",
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
                }
                ],
                "_links": {
                    "start": "/api/action/datastore_search?resource_id=d_9aba12b5527843afb0b2e8e4ed6ac6bd",
                    "next": "/api/action/datastore_search?resource_id=d_9aba12b5527843afb0b2e8e4ed6ac6bd&offset=100"
                },
                "total": 3
            }
        }
    """;

    private static final String NEXT_CCA_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_9aba12b5527843afb0b2e8e4ed6ac6bd",
                "records": [
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
                    "start": "/api/action/datastore_search?resource_id=d_9aba12b5527843afb0b2e8e4ed6ac6bd",
                    "next": "/api/action/datastore_search?resource_id=d_9aba12b5527843afb0b2e8e4ed6ac6bd&offset=100"
                },
                "total": 3
            }
        }
    """;

    private static final String PROGRAMME_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=d_b0697d22a7837a4eddf72efb66a36fc2";
    private static final String NEXT_PROGRAMME_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=d_b0697d22a7837a4eddf72efb66a36fc2&offset=100";

    private static final String PROGRAMME_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_b0697d22a7837a4eddf72efb66a36fc2",
                "records": [
                {
                 "_id": 1,
                 "school_name": "ADMIRALTY PRIMARY SCHOOL",
                 "moe_programme_desc": "Art Elective Programme"
                },
                {
                 "_id": 2,
                 "school_name": "ADMIRALTY PRIMARY SCHOOL",
                 "moe_programme_desc": "Art Elective Programme"
                }
                ],
                "_links": {
                    "start": "/api/action/datastore_search?resource_id=d_b0697d22a7837a4eddf72efb66a36fc2",
                    "next": "/api/action/datastore_search?resource_id=d_b0697d22a7837a4eddf72efb66a36fc2&offset=100"
                },
                "total": 3
            }
        }
    """;

    private static final String NEXT_PROGRAMME_JSON = """
        {
            "success": true,
            "result": {
                "resource_id": "d_b0697d22a7837a4eddf72efb66a36fc2",
                "records": [
                {
                 "_id": 3,
                 "school_name": "ADMIRALTY PRIMARY SCHOOL",
                 "moe_programme_desc": "Bicultural Studies Programme"
                }
                ],
                "_links": {
                    "start": "/api/action/datastore_search?resource_id=d_b0697d22a7837a4eddf72efb66a36fc2",
                    "next": "/api/action/datastore_search?resource_id=d_b0697d22a7837a4eddf72efb66a36fc2&offset=100"
                },
                "total": 3
            }
        }
    """;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("should return 200 with schools response when request valid")
    void shouldReturn200WithSchoolsResponseWhenRequestValid() throws Exception {
        mockServer.expect(requestTo(SCHOOL_GENERAL_INFORMATION_URL))
            .andRespond(withSuccess(SCHOOL_JSON, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(NEXT_SCHOOL_GENERAL_INFORMATION_URL))
            .andRespond(withSuccess(NEXT_SCHOOL_JSON, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(SUBJECT_URL))
            .andRespond(withSuccess(SUBJECT_JSON, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(NEXT_SUBJECT_URL))
            .andRespond(withSuccess(NEXT_SUBJECT_JSON, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(CCA_URL))
            .andRespond(withSuccess(CCA_JSON, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(NEXT_CCA_URL))
            .andRespond(withSuccess(NEXT_CCA_JSON, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(PROGRAMME_URL))
            .andRespond(withSuccess(PROGRAMME_JSON, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(NEXT_PROGRAMME_URL))
            .andRespond(withSuccess(NEXT_PROGRAMME_JSON, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/schools"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.schools.length()").value(2))
            .andExpect(jsonPath("$.schools[0].name").value("ADMIRALTY PRIMARY SCHOOL"))
            .andExpect(jsonPath("$.schools[0].website").value("https://admiraltypri.moe.edu.sg/"))
            .andExpect(jsonPath("$.schools[0].address").value("11 WOODLANDS CIRCLE"))
            .andExpect(jsonPath("$.schools[0].postalCode").value("738907"))
            .andExpect(jsonPath("$.schools[0].phoneNumber").value("63620598"))
            .andExpect(jsonPath("$.schools[0].faxNumber").value("63627512"))
            .andExpect(jsonPath("$.schools[0].email").value("ADMIRALTY_PS@MOE.EDU.SG"))
            .andExpect(jsonPath("$.schools[0].nearbyMrtStation").value("Admiralty Station"))
            .andExpect(jsonPath("$.schools[0].nearbyBusStation").value("TIBS 965, 964, 913"))
            .andExpect(jsonPath("$.schools[0].location").value("WOODLANDS"))
            .andExpect(jsonPath("$.schools[0].type").value("GOVERNMENT SCHOOL"))
            .andExpect(jsonPath("$.schools[0].natureCode").value("CO-ED SCHOOL"))
            .andExpect(jsonPath("$.schools[0].sessionCode").value("FULL DAY"))
            .andExpect(jsonPath("$.schools[0].level").value("PRIMARY"))
            .andExpect(jsonPath("$.schools[0].motherTongue1").value("CHINESE"))
            .andExpect(jsonPath("$.schools[0].motherTongue2").isEmpty())
            .andExpect(jsonPath("$.schools[0].motherTongue3").value("TAMIL"))
            .andExpect(jsonPath("$.schools[0].sapInd").value(false))
            .andExpect(jsonPath("$.schools[0].autonomousInd").value(true))
            .andExpect(jsonPath("$.schools[0].giftedInd").value(false))
            .andExpect(jsonPath("$.schools[0].ipInd").value(false))
            .andExpect(jsonPath("$.schools[0].subjects.length()").value(3))
            .andExpect(jsonPath("$.schools[0].subjects[0]").value("Art"))
            .andExpect(jsonPath("$.schools[0].subjects[1]").value("Bengali Language"))
            .andExpect(jsonPath("$.schools[0].subjects[2]").value("Chinese Language"))
            .andExpect(jsonPath("$.schools[0].ccas.length()").value(3))
            .andExpect(jsonPath("$.schools[0].ccas[0].name").value("ART AND CRAFTS"))
            .andExpect(jsonPath("$.schools[0].ccas[0].type").value("CLUBS AND SOCIETIES"))
            .andExpect(jsonPath("$.schools[0].ccas[1].name").value("CHINESE DANCE"))
            .andExpect(jsonPath("$.schools[0].ccas[1].type").value("VISUAL AND PERFORMING ARTS"))
            .andExpect(jsonPath("$.schools[0].ccas[2].name").value("CHOIR"))
            .andExpect(jsonPath("$.schools[0].ccas[2].type").value("VISUAL AND PERFORMING ARTS"))
            .andExpect(jsonPath("$.schools[0].programmes.length()").value(2))
            .andExpect(jsonPath("$.schools[0].programmes[0]").value("Art Elective Programme"))
            .andExpect(jsonPath("$.schools[0].programmes[1]").value("Bicultural Studies Programme"))
            .andExpect(jsonPath("$.schools[1].subjects.length()").value(0))
            .andExpect(jsonPath("$.schools[1].ccas.length()").value(0))
            .andExpect(jsonPath("$.schools[1].programmes.length()").value(0));

        assertEquals(2L, dbSchoolRepository.count());
        assertTrue(dbSchoolRepository.findOneByName("ADMIRALTY PRIMARY SCHOOL").isPresent());
        assertTrue(dbSchoolRepository.findOneByName("ADMIRALTY SECONDARY SCHOOL").isPresent());
    }
}
