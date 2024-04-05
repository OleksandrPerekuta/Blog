package com.blog.security;

import com.blog.user.dto.AdminDtoRegister;
import com.blog.user.dto.UserDtoLogin;
import com.blog.user.dto.UserDtoRegister;
import com.blog.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class RegistrationController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDtoRegister userDto, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage), HttpStatus.BAD_REQUEST);
        }
        try {
            String activationUrl = userService.createUser(userDto);
            return new ResponseEntity<>("registration succeed, activate your account, link is here : " + activationUrl, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid AdminDtoRegister adminDto, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage), HttpStatus.BAD_REQUEST);
        }
        try {
            String activationUrl = userService.createAdmin(adminDto);
            return new ResponseEntity<>("registration succeed, activate your account, link is here : " + activationUrl, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        return new ResponseEntity<>(userService.activateUser(token), HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDtoLogin request, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage), HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok(userService.authenticateUser(request));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
