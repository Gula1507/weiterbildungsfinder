package de.neuefische.backend.controller;

import de.neuefische.backend.model.Organization;
import de.neuefische.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    void getAllOrganizations_shouldReturnListOfOrganizations() throws Exception {
        Organization organization = new Organization(1, "testname", "testhomepage");
        organizationRepository.save(organization);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                                {
                                  "id": 1,
                                  "name": "testname",
                                  "homepage": "testhomepage"
                                
                                }
                                ]
                                """
                ));
    }

    @Test
    void shouldReturnEmptyListIntegrationTest() throws Exception {
        organizationRepository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


}