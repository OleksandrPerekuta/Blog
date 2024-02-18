package com.blog.tag.controller;

import com.blog.tag.dto.TagDtoCreation;
import com.blog.tag.dto.TagDtoResponse;
import com.blog.tag.service.TagService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tags")
@RestController
@AllArgsConstructor
public class TagController {
    private TagService tagService;
    @GetMapping
    public ResponseEntity<List<TagDtoResponse>> getAll(){
        return new ResponseEntity<>(tagService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TagDtoResponse> getTagById(@PathVariable Long id){
        return new ResponseEntity<>(tagService.getTagById(id),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Void> saveTag(@RequestBody @Valid TagDtoCreation tagDtoCreation){
        tagService.saveTag(tagDtoCreation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id){
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
