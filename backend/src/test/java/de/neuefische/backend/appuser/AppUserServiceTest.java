package de.neuefische.backend.appuser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    AppUserRepository mockedAppUserRepo = mock(AppUserRepository.class);
    PasswordEncoder mockedPasswordEncoder = mock(PasswordEncoder.class);

    AppUserService appUserService = new AppUserService(mockedAppUserRepo, mockedPasswordEncoder);
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser("1", "testUser", "testpassword", AppUserRole.USER);
    }

    @Test
    void findByUsername_shouldReturnAppUser_whenUserNameMatch() {
        String userName = "testUser";
        when(mockedAppUserRepo.findAppUserByUsername(userName)).thenReturn(Optional.of(appUser));

        AppUser expected = appUserService.findByUsername(userName);

        assertEquals(expected, appUser);
    }

    @Test
    void findByUsername_shouldThrowException_whenUserDoesNotExist() {
        String userName = "testUser2";
        when(mockedAppUserRepo.findAppUserByUsername(userName)).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> appUserService.findByUsername(userName));
    }

    @Test
    void getLoggedInUser_shouldReturnAppUserResponse_whenUserExists() {
        String username = "testUser";
        SecurityContext mockedSecurityContext = mock(SecurityContext.class);
        Authentication mockedAuthentication = mock(Authentication.class);
        User principal = new User(username, "testpassword", new ArrayList<>());
        when(mockedSecurityContext.getAuthentication()).thenReturn(mockedAuthentication);
        when(mockedAuthentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(mockedSecurityContext);

        when(mockedAppUserRepo.findAppUserByUsername(username)).thenReturn(Optional.of(appUser));

        AppUserResponse expected = appUserService.getLoggedInUser();

        verify(mockedAppUserRepo, times(1)).findAppUserByUsername(username);

        assertNotNull(expected);
        assertEquals(appUser.getId(), expected.id());
        assertEquals(appUser.getUsername(), expected.username());
        assertEquals(appUser.getRole(), expected.appUserRole());

    }

    @Test
    void register_shouldReturnAppUserResponse_withUserNameAndRoleFromRegister() {
        AppUserRegister appUserRegister = new AppUserRegister(appUser.getUsername(), "123");
        AppUserResponse expected = new AppUserResponse(appUser.getId(), appUser.getUsername(), appUser.getRole());
        when(mockedAppUserRepo.save(any(AppUser.class))).thenReturn(appUser);
        AppUserResponse actual = appUserService.register(appUserRegister);

        verify(mockedAppUserRepo).save(any(AppUser.class));
        verify(mockedPasswordEncoder).encode("123");
        assertEquals(expected, actual);
    }
}