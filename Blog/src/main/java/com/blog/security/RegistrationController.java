package com.blog.security;

import com.blog.user.dto.UserDtoRegister;
import com.blog.user.dto.UserDtoLogin;
import com.blog.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class RegistrationController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDtoRegister userDto) {
        try {
            String activationUrl= userService.createUser(userDto);
            return new ResponseEntity<>("registration succeed, activate your account, link is here : " + activationUrl, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        return new ResponseEntity<>(userService.activateUser(token),HttpStatus.ACCEPTED);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationTokenResponse> login(@RequestBody UserDtoLogin request){
        return ResponseEntity.ok(userService.authenticateUser(request));
    }

}
