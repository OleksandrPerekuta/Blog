package com.blog.role.service;

import com.blog.role.dto.RoleDtoCreate;
import com.blog.role.dto.RoleDtoPatch;
import com.blog.role.dto.RoleDtoResponse;
import com.blog.role.entity.RoleEntity;
import com.blog.role.mapper.RoleMapper;
import com.blog.role.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;
    @Transactional(readOnly = true)
    public List<RoleDtoResponse> getAll(){
        List<RoleEntity> entities=roleRepository.findAll();
        return entities.stream().map(roleMapper::mapToDto).toList();
    }
    @Transactional(readOnly = true)
    public RoleDtoResponse getRoleById(Long id){
        RoleEntity entity=roleRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Role not found with id: "+id));
        return roleMapper.mapToDto(entity);
    }
    @Transactional
    public void saveRole(RoleDtoCreate roleDto){
        roleRepository.save(roleMapper.mapToEntity(roleDto));
    }
    @Transactional
    public void deleteRole(Long id){
        roleRepository.deleteById(id);
    }
    @Transactional
    public RoleDtoResponse changeNameRole(RoleDtoPatch patch,Long id){
        RoleEntity entity=roleRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Role not found with id: "+id));
        entity.setName(patch.getName());
        return roleMapper.mapToDto(entity);
    }
    @Transactional
    public RoleEntity getRoleEntityByName(String name){
        return roleRepository.getByName(name).orElseThrow(()->new EntityNotFoundException("Role not found with name: "+name));
    }
}
