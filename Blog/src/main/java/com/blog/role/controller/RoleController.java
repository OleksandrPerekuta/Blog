package com.blog.role.controller;

import com.blog.role.dto.RoleDtoCreate;
import com.blog.role.dto.RoleDtoPatch;
import com.blog.role.dto.RoleDtoResponse;
import com.blog.role.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RequestMapping("/roles")
@RestController
@AllArgsConstructor
public class RoleController {
    private RoleService roleService;
    @GetMapping
    public ResponseEntity<List<RoleDtoResponse>> getAll(){
        return new ResponseEntity<>(roleService.getAll(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RoleDtoResponse> getRoleById(@PathVariable Long id){
        return new ResponseEntity<>(roleService.getRoleById(id),HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<RoleDtoResponse> changeNameRole(@PathVariable Long id, @RequestBody RoleDtoPatch patch){
        return new ResponseEntity<>(roleService.changeNameRole(patch,id),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Void> saveRole(@RequestBody @Valid RoleDtoCreate roleDtoCreate){
        roleService.saveRole(roleDtoCreate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
