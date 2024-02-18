package com.blog.category.controller;

import com.blog.category.dto.CategoryDtoCreation;
import com.blog.category.dto.CategoryDtoResponse;
import com.blog.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/categories")
@RestController
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;
    @GetMapping
    public ResponseEntity<List<CategoryDtoResponse>> getAll(){
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDtoResponse> getCategoryById(@PathVariable Long id){
        return new ResponseEntity<>(categoryService.getCategoryById(id),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Void> saveCategory(@RequestBody @Valid CategoryDtoCreation categoryDtoCreation){
        categoryService.saveCategory(categoryDtoCreation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
