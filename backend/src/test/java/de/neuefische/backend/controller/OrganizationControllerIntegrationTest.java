package de.neuefische.backend.controller;

import de.neuefische.backend.model.Organization;
import de.neuefische.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrganizationRepository organizationRepository;

    @BeforeEach
    void setUp() {
        organizationRepository.deleteAll();
    }

    @Test
    void getAllOrganizations_shouldReturnListOfOrganizations() throws Exception {
        Organization organization = new Organization("1", "testname", "testhomepage");
        organizationRepository.save(organization);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(
                        """
                                [
                                {
                                  "id": "1",
                                  "name": "testname",
                                  "homepage": "testhomepage"
                                }
                                ]
                                """
                ));
    }

    @Test
    void getAllOrganizations_shouldReturnEmptyListIntegrationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
                .andExpect(MockMvcResultMatchers.status().isCreated())
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
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}