package com.blog.user.controller;

import com.blog.exception.NullValueException;
import com.blog.user.dto.UserDtoCreate;
import com.blog.user.dto.UserDtoPasswordReset;
import com.blog.user.dto.UserDtoPatch;
import com.blog.user.dto.UserDtoResponse;
import com.blog.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> changesUser(@PathVariable Long id, @RequestBody @Valid UserDtoPatch userDtoPatch){
        try{
            return new ResponseEntity<>(userService.patchUser(userDtoPatch,id),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody @Valid UserDtoCreate userDtoCreate, BindingResult bindingResult) throws NullValueException{
        if (bindingResult.hasErrors()){
            String message = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",\n"));
            return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
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
    @PatchMapping("/password_reset/{id}")
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid UserDtoPasswordReset userDtoPasswordReset,
            @PathVariable Long id,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",\n"));
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(userService.resetPassword(userDtoPasswordReset, id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(",\n"));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}