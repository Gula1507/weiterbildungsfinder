package de.neuefische.backend.appuser;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppUserDetailsServiceTest {

    AppUserService mockedAppUserService=mock(AppUserService.class);

    AppUserDetailsService appUserDetailsService=new AppUserDetailsService(mockedAppUserService);

    @Test
    void loadUserByUsername() {
        String username="testUser";
        AppUser   appUser = new AppUser("1", "testUser", "testpassword", AppUserRole.USER);
        when(mockedAppUserService.findByUsername(username)).thenReturn(appUser);
        User expected=new User(appUser.getUsername(),appUser.getPassword(), List.of(
                new SimpleGrantedAuthority("ROLE " + appUser.getRole())
        ));
        User actual= (User) appUserDetailsService.loadUserByUsername(username);
        assertEquals(expected,actual);
    }
}