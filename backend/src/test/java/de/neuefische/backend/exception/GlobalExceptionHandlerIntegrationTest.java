package de.neuefische.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleException_shouldReturnErrorMessage_whenUriNotFound() throws Exception {
        ErrorMessage errorMessage1 = new ErrorMessage("An unexpected error occurred: No static resource api/organizationsss.");
        mockMvc.perform(get("/api/organizationsss"))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage1.message()));
    }
}