package com.blog.user.service;

import com.blog.exception.UserAlreadyExistsException;
import com.blog.exception.UserNotActivatedException;
import com.blog.role.entity.RoleEntity;
import com.blog.role.repository.RoleRepository;
import com.blog.user.dto.UserDtoCreate;
import com.blog.user.dto.UserDtoPasswordReset;
import com.blog.user.dto.UserDtoPatch;
import com.blog.user.dto.UserDtoResponse;
import com.blog.user.entity.UserEntity;
import com.blog.user.mapper.UserMapper;
import com.blog.user.repository.UserRepository;
import com.blog.user_role.service.UserRoleService;
import com.blog.verificationToken.entity.VerificationTokenEntity;
import com.blog.verificationToken.repository.VerificationTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserRoleService userRoleService;
    private RoleRepository roleRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private UserMapper userMapper;
    @Transactional(readOnly = true)
    public List<UserDtoResponse> getAll(){
        List<UserEntity> users=userRepository.findAll();
        return users.stream().map(userMapper::mapToDto).toList();
    }
    @Transactional(readOnly = true)
    public UserDtoResponse getUserById(Long id) throws EntityNotFoundException{
        UserEntity entity=userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User with id: "+id+" not found"));
        return userMapper.mapToDto(entity);
    }
    @Transactional
    public String saveUser(UserDtoCreate userDtoCreate){
        try{
            if (userRepository.findByUsername(userDtoCreate.getUsername()).isPresent()){
                throw new UserAlreadyExistsException("User with this username already exists");
            }
            if (userRepository.findByEmail(userDtoCreate.getEmail().toLowerCase()).isPresent()){
                throw new UserAlreadyExistsException("User with this email already exists");
            }
            UserEntity userEntity=userMapper.mapToEntity(userDtoCreate);
            userEntity.setEmail(userEntity.getEmail().toLowerCase());
            RoleEntity roleEntity=roleRepository.getByName("USER").orElseThrow(()->new EntityNotFoundException("Role with name \"USER\" is not found"));
            userRepository.save(userEntity);
            userRoleService.setUserRole(userEntity,roleEntity);
            return generateVerificationToken(userEntity);
        }catch (Exception e){
            return e.getMessage();
        }
    }
    private String generateVerificationToken(UserEntity user){
        String token = UUID.randomUUID().toString();
        System.out.println("created token: "+token);
        OffsetDateTime expirationDateTime = OffsetDateTime.now().plusHours(1);

        VerificationTokenEntity verificationToken = new VerificationTokenEntity();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpirationDate(expirationDateTime);
        verificationTokenRepository.save(verificationToken);
        return "http://localhost:8080/users/activate?token=" + token;
    }
    @Transactional
    public void deleteUser(Long id){
        verificationTokenRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
    @Transactional
    public UserDtoResponse patchUser(UserDtoPatch patch,Long id) throws UserAlreadyExistsException, UserNotActivatedException {
        UserEntity userEntity=userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User not found with id: "+id));
        if (!userEntity.isEnabled()){
            throw new UserNotActivatedException("User is not activated");
        }
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        Optional.ofNullable(patch.getUsername()).ifPresent(userEntity::setUsername);
        return userMapper.mapToDto(userEntity);
    }
    @Transactional
    public String activateUser(String token){
        VerificationTokenEntity verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("No token found"));
        if (OffsetDateTime.now().isAfter(verificationToken.getExpirationDate())) {
            verificationTokenRepository.delete(verificationToken);
            return "token is not valid anymore";
        }
        if (verificationToken.getUser().isEnabled()) {
            return "Account is already activated";
        }
        UserEntity user = userRepository.findById(verificationToken.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "Account activated";
    }

    public String resetPassword(UserDtoPasswordReset userDtoPasswordReset,Long id) throws EntityNotFoundException, UserNotActivatedException {
        UserEntity userEntity=userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User with id: "+id+" not found"));
        if (!userEntity.isEnabled()){
            throw new UserNotActivatedException("User is not activated");
        }
        userEntity.setPassword(userDtoPasswordReset.getPassword());
        userRepository.save(userEntity);
        return "Password changed";
    }
}
