package de.neuefische.backend.appuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AppUserRepository appUserRepository;

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
        appUser = new AppUser("1", "testUser", "testpassword", AppUserRole.USER);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getLoggedInUser_shouldReturnAppUserResponse_withUsernameAndRole() throws Exception {

        appUserRepository.save(appUser);
        mockMvc.perform(get("/api/users/me")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testUser")).andExpect(MockMvcResultMatchers.jsonPath("$.appUserRole").value("USER"));

    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getLoggedInUser_shouldReturnError_whenUserNotExist() throws Exception {

        mockMvc.perform(get("/api/users/me")).andExpect(MockMvcResultMatchers.status().isInternalServerError());

    }

    @Test
    void register_shouldReturnAppUserResponse_withSameNameAndRoleUser() throws Exception {
        appUserRepository.save(appUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                      "username": "testUser",
                      "password": "123"
                }
                """)).andExpect(status().isCreated()).andExpect(content().json("""
                 {
                                     "username": "testUser",
                                     "appUserRole": "USER"
                                 }
                """));
    }

    @Test
    void logout_shouldReturnUnauthorized_whenUserNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/logout"))
                .andExpect(status().isUnauthorized());
    }
}