package de.neuefische.backend.organization;

import de.neuefische.backend.api.service.ArbeitsagenturApiService;
import de.neuefische.backend.organization.model.Organization;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", authorities = "ROLE ADMIN")
class OrganizationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrganizationRepository organizationRepository;

    @MockBean
    ArbeitsagenturApiService mockedApiService;

    private Organization testOrganization;

    @BeforeEach
    void setUp() {
        organizationRepository.deleteAll();
        testOrganization = new Organization("1", 100L,"testname", "testhomepage", "testemail", "testaddress",
                new ArrayList<>(), 0.0,new ArrayList<>());
    }

    @Test
    void getAllOrganizations_shouldReturnPagedListOfOrganizationsByDefault() throws Exception {
        organizationRepository.save(testOrganization);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations")).andExpect(status().isOk()).andExpect(content().json("""
                   { "content":
                    [
                        {
                            "id": "1",
                            "name": "testname",
                            "homepage": "testhomepage",
                            "email": "testemail",
                            "address": "testaddress",
                            "reviews": [],
                            "averageRating": 0.0
                        }
                    ],
                     "pageable": {
                        "sort": {
                            "sorted": false,
                            "unsorted": true,
                            "empty": true
                        },
                        "pageNumber": 0,
                        "pageSize": 10,
                        "offset": 0,
                        "unpaged": false,
                        "paged": true
                     },
                     "totalPages": 1,
                     "totalElements": 1,
                     "last": true,
                     "size": 10,
                     "number": 0,
                     "sort": {
                        "sorted": false,
                        "unsorted": true,
                        "empty": true
                     },
                     "first": true,
                     "numberOfElements": 1,
                     "empty": false
                   }
                """));

    }


    @Test
    void getAllOrganizations_shouldReturnEmptyListWhenNoOrganisationsInRepo() throws Exception {
        organizationRepository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations")).andExpect(status().isOk()).andExpect(content().json("""
                 { "content":[],
                  "pageable": {
                                                           "pageNumber": 0,
                                                           "pageSize": 10,
                                                           "sort": {
                                                               "empty": true,
                                                               "sorted": false,
                                                               "unsorted": true
                                                           },
                                                           "offset": 0,
                                                           "paged": true,
                                                           "unpaged": false
                                                       },
                                                       "last": true,
                                                       "totalPages": 0,
                                                       "totalElements": 0,
                                                       "first": true,
                                                       "size": 10,
                                                       "number": 0,
                                                       "sort": {
                                                           "empty": true,
                                                           "sorted": false,
                                                           "unsorted": true
                                                       },
                                                       "numberOfElements": 0,
                                                       "empty": true
                                   }
                """));
    }


    @Test
    void addOrganization_shouldSaveOrganizationWithId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/organizations").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "name": "testorganization",
                "homepage": "testhomepage"
                }
                """)).andExpect(status().isCreated()).andExpect(content().json("""
                {
                "name": "testorganization",
                "homepage": "testhomepage"
                }
                """)).andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    void addOrganization_returnBadRequest_whenOrganizationNameMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/organizations").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "homepage": "testhomepage"
                }
                """)).andExpect(status().isBadRequest());
    }

    @Test
    void getOrganizationById_shouldReturnOrganizationDTO() throws Exception {
        organizationRepository.save(testOrganization);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations/{id}", "1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.name").value("testname")).andExpect(jsonPath("$.homepage").value("testhomepage")).andExpect(jsonPath("$.email").value("testemail")).andExpect(jsonPath("$.address").value("testaddress"));
    }

    @Test
    void getOrganizationById_shouldReturnNotFound_whenOrganizationDoesNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations/{id}", "nonexistentId").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Weiterbildungsanbieter mit id: nonexistentId nicht gefunden"));
    }

    @Test
    void updateOrganization_shouldReturnOrganizationWithNewHomepage_whenHomepageUpdate() throws Exception {
        organizationRepository.save(testOrganization);
        String id = "1";
        String requestBody = """
                {
                  "name": "testname",
                  "homepage": "newhomepage",
                  "email": "testemail",
                  "address": "testaddress",
                  "reviews": [],
                  "averageRating": 0.0
                }
                """;
        String expectedResponseBody = """
                {
                  "name": "testname",
                  "homepage": "newhomepage",
                  "email": "testemail",
                  "address": "testaddress",
                  "reviews": [],
                  "averageRating": 0.0
                }
                """;

        mockMvc.perform(put("/api/organizations/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void updateOrganization_shouldReturn404_whenOrganizationNotFound() throws Exception {

        String nonExistingId = "999";
        String requestBody = """
                {
                  "name": "testname",
                  "homepage": "newhomepage",
                  "email": "testemail",
                  "address": "testaddress",
                  "reviews": [],
                  "averageRating": 0.0
                }
                """;

        mockMvc.perform(put("/api/organizations/{id}", nonExistingId).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Weiterbildungsanbieter mit id: 999 nicht gefunden"));
    }

    @Test
    void deleteOrganization_whenExists_shouldReturnNoContent() throws Exception {
        organizationRepository.save(testOrganization);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/organizations/1")).andExpect(status().isNoContent());
        Assertions.assertFalse(organizationRepository.existsById(testOrganization.id()));
    }

    @Test
    void deleteOrganization_shouldReturn404_ifOrganizationNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/organizations/2")).andExpect(status().isNotFound());
    }

    @Test
    void refreshOrganizations_shouldReturnListOfOrganizations() throws Exception {
        when(mockedApiService.loadAllOrganizations()).thenReturn(List.of(testOrganization));

        mockMvc.perform(put("/api/organizations/refresh")).andExpect(status().isOk()).andExpect(content().json("""
                    [
                        {
                            "id": "1",
                            "name": "testname",
                            "homepage": "testhomepage",
                            "email": "testemail",
                            "address": "testaddress",
                            "reviews": [],
                            "averageRating": 0.0
                        }
                    ]
                """));

    }

    @Test
    void refreshOrganizations_shouldReturnEmptyList_whenNoNewOrganizations() throws Exception {
        organizationRepository.save(testOrganization);
        when(mockedApiService.loadAllOrganizations()).thenReturn(List.of(testOrganization));
        mockMvc.perform(put("/api/organizations/refresh"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void addReview_shouldReturnOrganizationWithReview_whenDTOReviewValid() throws Exception {
        organizationRepository.save(testOrganization);

        mockMvc.perform(post("/api/organizations/1/reviews").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "author": "John",
                "comment": "Not good",
                "starNumber": 2
                }
                """)).andExpect(status().isCreated()).andExpect(content().json("""
                
                {
                   "id": "1",
                   "name": "testname",
                   "homepage": "testhomepage",
                   "email": "testemail",
                  "address": "testaddress",
                  "reviews": [
                      {
                          "author": "testuser",
                          "comment": "Not good",
                          "starNumber": 2
                      }
                  ],
                  "averageRating": 2
                }
                """)).andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    void addReview_shouldReturnWarnMessage_whenDTOReviewNotValid() throws Exception {
        organizationRepository.save(testOrganization);

        mockMvc.perform(post("/api/organizations/1/reviews").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "author": "John",
                "comment": "Not good",
                "starNumber": 0
                }
                """)).andExpect(status().isBadRequest()).andExpect(content().json("""
                {
                 "message": "Validation failed: starNumber: must be greater than or equal to 1"
                }
                """));
    }
}
