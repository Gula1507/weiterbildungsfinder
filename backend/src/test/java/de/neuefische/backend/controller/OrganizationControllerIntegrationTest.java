package de.neuefische.backend.controller;

import de.neuefische.backend.model.Organization;
import de.neuefische.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrganizationRepository organizationRepository;

    private Organization testOrganization;

    @BeforeEach
    void setUp() {
        organizationRepository.deleteAll();
        testOrganization = new Organization("1", "testname", "testhomepage",
                "testemail", "testaddress");
    }

    @Test
    void getAllOrganizations_shouldReturnListOfOrganizations() throws Exception {
        organizationRepository.save(testOrganization);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                {
                                  "id": "1",
                                  "name": "testname",
                                  "homepage": "testhomepage",
                                  "email": "testemail",
                                  "address": "testaddress"
                                }
                                ]
                                """
                ));
    }

    @Test
    void getAllOrganizations_shouldReturnEmptyListIntegrationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }


    @Test
    void addOrganization_shouldSaveOrganizationWithId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "testorganization",
                                "homepage": "testhomepage"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        """
                                {
                                "name": "testorganization",
                                "homepage": "testhomepage"
                                }
                                """
                )).andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    void addOrganization_returnBadRequest_whenOrganizationNameMissing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "homepage": "testhomepage"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrganizationById_shouldReturnOrganizationDTO() throws Exception {
        organizationRepository.save(testOrganization);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations/{id}", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testname"))
                .andExpect(jsonPath("$.homepage").value("testhomepage"))
                .andExpect(jsonPath("$.email").value("testemail"))
                .andExpect(jsonPath("$.address").value("testaddress"));
    }

    @Test
    void getOrganizationById_shouldReturnNotFound_whenOrganizationDoesNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations/{id}", "nonexistentId")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Weiterbildungsanbieter mit id: nonexistentId nicht gefunden"));
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
                  "address": "testaddress"
                }
                """;
        String expectedResponseBody = """
                {
                  "name": "testname",
                  "homepage": "newhomepage",
                  "email": "testemail",
                  "address": "testaddress"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/organizations/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
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
                  "address": "testaddress"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/organizations/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Weiterbildungsanbieter mit id: 999 nicht gefunden"));
    }

    @Test
    void deleteOrganization_whenExists_shouldReturnNoContent() throws Exception {
        organizationRepository.save(testOrganization);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/organizations/1"))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(organizationRepository.existsById(testOrganization.id()));
    }

    @Test
    void deleteOrganization_shouldReturn404_ifOrganizationNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/organizations/2"))
                .andExpect(status().isNotFound());
    }
}