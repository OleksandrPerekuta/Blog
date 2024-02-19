package com.blog.user.service;

import com.blog.exception.UserAlreadyExistsException;
import com.blog.exception.UserNotActivatedException;
import com.blog.role.entity.RoleEntity;
import com.blog.role.repository.RoleRepository;
import com.blog.security.AuthenticationTokenResponse;
import com.blog.security.JwtService;
import com.blog.user.dto.*;
import com.blog.user.entity.UserEntity;
import com.blog.user.mapper.UserMapper;
import com.blog.user.repository.UserRepository;
import com.blog.verificationToken.entity.VerificationTokenEntity;
import com.blog.verificationToken.repository.VerificationTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    @Transactional
    public String createUser(UserDtoRegister userDtoRegister) throws UserAlreadyExistsException{
            if (userRepository.findByUsername(userDtoRegister.getUsername()).isPresent()){
                throw new UserAlreadyExistsException("User with this username already exists");
            }
            if (userRepository.findByEmail(userDtoRegister.getEmail().toLowerCase()).isPresent()){
                throw new UserAlreadyExistsException("User with this email already exists");
            }
            UserEntity userEntity=userMapper.mapToEntity(userDtoRegister);
            userEntity.setEmail(userEntity.getEmail().toLowerCase());
            RoleEntity roleEntity=roleRepository.getByName("USER").orElseThrow(()->new EntityNotFoundException("Role with name \"USER\" is not found"));
            userEntity.setRoles(Set.of(roleEntity));

            userRepository.save(userEntity);
            return generateLinkWithVerificationToken(userEntity);
    }
    @Bean
    public void initialRoleValues(){
        roleRepository.save(new RoleEntity(1L,"ROLE_ADMIN"));
        roleRepository.save(new RoleEntity(2L,"ROLE_USER"));
    }
    private String generateLinkWithVerificationToken(UserEntity user){
        String token = UUID.randomUUID().toString();
        System.out.println("created token: "+token);
        OffsetDateTime expirationDateTime = OffsetDateTime.now().plusHours(1);

        VerificationTokenEntity verificationToken = new VerificationTokenEntity();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpirationDate(expirationDateTime);
        verificationTokenRepository.save(verificationToken);
        return "http://localhost:8080/auth/activate?token=" + token;
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

    public AuthenticationTokenResponse authenticateUser(UserDtoLogin request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        UserEntity user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new EntityNotFoundException("User not found"));
        String jwtToken=jwtService.generateToken(user);
        return AuthenticationTokenResponse.builder().token(jwtToken).build();
    }

}
