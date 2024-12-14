package de.neuefische.backend.appuser;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/me")
    public AppUserResponse getLoggedInUser() {
        return appUserService.getLoggedInUser();
    }

    @PostMapping("/login")
    public void login() {
        // This method is only to trigger the login process
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserResponse register(@RequestBody AppUserRegister appUserRegister) {
        return appUserService.register(appUserRegister);
    }
}
