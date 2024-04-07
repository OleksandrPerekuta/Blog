package com.blog.category.service;

import com.blog.category.dto.CategoryDtoCreation;
import com.blog.category.dto.CategoryDtoResponse;
import com.blog.category.entity.CategoryEntity;
import com.blog.category.mapper.CategoryMapper;
import com.blog.category.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryDtoResponse> getAll() {
        List<CategoryEntity> entityList = categoryRepository.findAll();
        return entityList.stream().map(categoryMapper::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDtoResponse getCategoryById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " is not found"));
        return categoryMapper.mapToDto(entity);
    }

    @Transactional
    public CategoryDtoResponse saveCategory(CategoryDtoCreation categoryDtoCreation) {
        return categoryMapper.mapToDto(
                categoryRepository.save(categoryMapper.mapToEntity(categoryDtoCreation)));
    }

    @Transactional
    public void deactivateCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                        .orElseThrow(()->new EntityNotFoundException("Category not found with id: "+id));
        category.setActive(false);
    }

    @Transactional
    public CategoryDtoResponse getByName(String name) {
        CategoryEntity entity = categoryRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Category not dound with name: " + name));
        return categoryMapper.mapToDto(entity);
    }

    @Transactional
    public CategoryEntity getOrCreateByName(String name) {
        if (categoryRepository.findByName(name).isEmpty()) {
            this.saveCategory(new CategoryDtoCreation(name));
        }
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with name: " + name));
    }
}
