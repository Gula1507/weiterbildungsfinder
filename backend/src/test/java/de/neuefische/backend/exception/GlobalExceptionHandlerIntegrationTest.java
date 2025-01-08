package de.neuefische.backend.exception;

import de.neuefische.backend.organization.OrganizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrganizationService organizationService;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void handleException_shouldReturnErrorMessage_whenDataBaseNotAvailable() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(
                "An unexpected error occurred. Please contact support if the " + "problem persists.");

        when(organizationService.getAllOrganizations(anyInt(), anyInt(), anyString())).thenThrow(
                new DataAccessException("Database error") {
                });

        mockMvc.perform(get("/api/organizations"))
               .andExpect(status().isInternalServerError())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                               .value(errorMessage.message()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void handleException_shouldReturnMessageThePageNotExist_whenUriFalse() throws Exception {
        ErrorMessage errorMessage1 = new ErrorMessage("The page 'api/organizationsss' doesn't exist");
        mockMvc.perform(get("/api/organizationsss"))
               .andExpect(status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                               .value(errorMessage1.message()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void handleHttpMessageNotReadableException_shouldReturnInvalidRequestBody_whenBodyMissing() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(
                "Invalid request body. Please ensure the request contains valid " + "JSON.");
        mockMvc.perform(post("/api/organizations"))
               .andExpect(status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                               .value(errorMessage.message()));
    }
}