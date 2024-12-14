package de.neuefische.backend.appuser;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser findByUsername(String username) {
        return appUserRepository.findAppUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "User mit name" + username + " not found"));
    }

    public AppUserResponse getLoggedInUser() {
        var principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = findByUsername(principal.getUsername());
        return new AppUserResponse(appUser.getId(), appUser.getUsername(),appUser.getRole());
    }

    public AppUserResponse register(AppUserRegister appUserRegister) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserRegister.username());
        appUser.setPassword(passwordEncoder.encode(appUserRegister.password()));
        appUser.setRole(AppUserRole.USER);
        appUser = appUserRepository.save(appUser);
        return new AppUserResponse(appUser.getId(), appUser.getUsername(),appUser.getRole());
    }
}
