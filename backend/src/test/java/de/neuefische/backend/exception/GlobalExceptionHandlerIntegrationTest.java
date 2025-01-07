package de.neuefische.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void handleException_shouldReturnErrorMessage_whenUriNotFound() throws Exception {
        ErrorMessage errorMessage1 = new ErrorMessage("An unexpected error occurred: No static resource api/organizationsss.");
        mockMvc.perform(get("/api/organizationsss"))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage1.message()));
    }

    @Test
    @WithMockUser(username="testuser", roles="USER")
    void handleHttpMessageNotReadableException_shouldReturnInvalidRequestBody_whenBodyMissing() throws Exception {
        ErrorMessage errorMessage=new ErrorMessage("Invalid request body. Please ensure the request contains valid JSON.");
        mockMvc.perform(post("/api/organizations"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage.message()));
    }
}