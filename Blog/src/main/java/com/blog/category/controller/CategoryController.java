package com.blog.category.controller;

import com.blog.category.dto.CategoryDtoCreation;
import com.blog.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDtoCreation categoryDtoCreation) {
        return new ResponseEntity<>(categoryService.saveCategory(categoryDtoCreation),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
