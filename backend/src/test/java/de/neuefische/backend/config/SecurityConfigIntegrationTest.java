package de.neuefische.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetOrganizationsEndpointPermittedForEveryone() throws Exception {
        mockMvc.perform(get("/api/organizations"))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUserEndpointPermittedForEveryone() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "testuser",
                                "password": "password"
                            }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ROLE ADMIN")
    void testDeleteOrganizationEndpointAuthorizedForAdmin() throws Exception {
        mockMvc.perform(delete("/api/organizations/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteOrganizationEndpointForbiddenForNonAdmin() throws Exception {
        mockMvc.perform(delete("/api/organizations/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteOrganizationEndpointUnauthorizedWithoutLogin() throws Exception {
        mockMvc.perform(delete("/api/organizations/1"))
                .andExpect(status().isUnauthorized());
    }
}