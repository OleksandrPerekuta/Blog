package com.blog.user.controller;

import com.blog.exception.NullValueException;
import com.blog.user.dto.UserDtoCreate;
import com.blog.user.dto.UserDtoPatch;
import com.blog.user.dto.UserDtoResponse;
import com.blog.user.repository.UserRepository;
import com.blog.user.service.UserService;
import com.blog.verificationToken.repository.VerificationTokenRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;
    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAll(){
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserDtoResponse> changesUser(@PathVariable Long id, @RequestBody @Valid UserDtoPatch userDtoPatch){
        return new ResponseEntity<>(userService.patchUser(userDtoPatch,id),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody @Valid UserDtoCreate userDtoCreate, Errors errors) throws NullValueException{
        if (errors.hasErrors()) {
            throw new NullValueException(errors);
        }
        return new ResponseEntity<>(userService.saveUser(userDtoCreate),HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("token") String token){
        return new ResponseEntity<>(userService.activateUser(token),HttpStatus.OK);
    }
}