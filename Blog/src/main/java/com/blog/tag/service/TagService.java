package com.blog.tag.service;

import com.blog.tag.dto.TagDtoCreation;
import com.blog.tag.dto.TagDtoResponse;
import com.blog.tag.entity.TagEntity;
import com.blog.tag.mapper.TagMapper;
import com.blog.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {
    private TagRepository tagRepository;
    private TagMapper tagMapper;
    @Transactional(readOnly = true)
    public List<TagDtoResponse> getAll(){
        List<TagEntity> entityList=tagRepository.findAll();
        return entityList.stream().map(tagMapper::mapToDto).toList();
    }
    @Transactional(readOnly = true)
    public TagDtoResponse getTagById(Long id){
        TagEntity entity=tagRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Tag with id "+id+" is not found"));
        return tagMapper.mapToDto(entity);
    }
    @Transactional
    public void saveTag(TagDtoCreation tagDtoCreation){
        tagRepository.save(tagMapper.mapToEntity(tagDtoCreation));
    }
    @Transactional
    public void deleteTag(Long id){
        tagRepository.deleteById(id);
    }
    @Transactional
    public TagEntity getOrCreateByName(String name){
        if (tagRepository.findByName(name).isEmpty()){
            this.saveTag(new TagDtoCreation(name));
        }
        return tagRepository.findByName(name)
                .orElseThrow(()->new EntityNotFoundException("Tag not found with name: "+name));
    }

}
