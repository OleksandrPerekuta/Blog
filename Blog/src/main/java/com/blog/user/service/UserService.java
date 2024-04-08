package com.blog.user.service;

import com.blog.exception.TokenException;
import com.blog.exception.UserAlreadyExistsException;
import com.blog.exception.UserNotActivatedException;
import com.blog.role.entity.RoleEntity;
import com.blog.role.repository.RoleRepository;
import com.blog.security.AuthenticationTokenResponse;
import com.blog.security.JwtService;
import com.blog.user.dto.AdminDtoRegister;
import com.blog.user.dto.UserDtoLogin;
import com.blog.user.dto.UserDtoPasswordReset;
import com.blog.user.dto.UserDtoRegister;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public String createUser(UserDtoRegister userDtoRegister) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(userDtoRegister.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        if (userRepository.findByEmail(userDtoRegister.getEmail().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        UserEntity userEntity = userMapper.mapToEntity(userDtoRegister);
        userEntity.setEmail(userEntity.getEmail().toLowerCase());
        RoleEntity roleEntity = roleRepository.getByName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException("Role with name \"USER\" is not found"));
        userEntity.setRoles(Set.of(roleEntity));

        userRepository.save(userEntity);
        return generateLinkWithVerificationToken(userEntity);
    }

    @Transactional
    public String createAdmin(AdminDtoRegister adminDtoRegister) throws UserAlreadyExistsException, IOException, TokenException {
        if (userRepository.findByUsername(adminDtoRegister.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        if (userRepository.findByEmail(adminDtoRegister.getEmail().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        String key = new String(Files.readAllBytes(Path.of("src/main/resources/key.txt")));
        System.out.println("KEY IS " + key);// xd :)
        if (!adminDtoRegister.getSecret().equals(key)) {
            throw new TokenException("Key is not correct");
        }
        UserEntity userEntity = userMapper.mapToEntity(adminDtoRegister);
        userEntity.setEmail(userEntity.getEmail().toLowerCase());
        RoleEntity userRole = roleRepository.getByName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException("Role with name \"USER\" is not found"));
        RoleEntity adminRole = roleRepository.getByName("ROLE_ADMIN").orElseThrow(() -> new EntityNotFoundException("Role with name \"ADMIN\" is not found"));
        userEntity.setRoles(Set.of(userRole, adminRole));
        userRepository.save(userEntity);
        return generateLinkWithVerificationToken(userEntity);
    }

    @Bean
    public void initialRoleValues() {
        roleRepository.save(new RoleEntity(1L, "ROLE_ADMIN"));
        roleRepository.save(new RoleEntity(2L, "ROLE_USER"));
    }

    private String generateLinkWithVerificationToken(UserEntity user) {
        String token = UUID.randomUUID().toString();
        OffsetDateTime expirationDateTime = OffsetDateTime.now().plusHours(1);

        VerificationTokenEntity verificationToken = new VerificationTokenEntity();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpirationDate(expirationDateTime);
        verificationTokenRepository.save(verificationToken);
        return "http://localhost:8080/auth/activate?token=" + token;
    }


    @Transactional
    public String activateUser(String token) {
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

    //todo: change logic so link would be sent on demand as a token an only then password would be reset
    public String resetPassword(UserDtoPasswordReset userDtoPasswordReset, Long id) throws EntityNotFoundException, UserNotActivatedException {
        UserEntity userEntity = userRepository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        if (!userEntity.isEnabled()) {
            throw new UserNotActivatedException("User is not activated");
        }
        userEntity.setPassword(userDtoPasswordReset.getPassword());
        userRepository.save(userEntity);
        return "Password changed";
    }

    public AuthenticationTokenResponse authenticateUser(UserDtoLogin request) throws EntityNotFoundException {
        UsernamePasswordAuthenticationToken pat=new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        //add delay in case of incorrect credentials to prevent brute force
        if (!pat.isAuthenticated()){
            try {
                Thread.sleep(2 * 1000); // 2 seconds
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        authenticationManager.authenticate(pat);
        UserEntity user = userRepository.findByEmail(request.getEmail())
                                            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationTokenResponse.builder().token(jwtToken).build();
    }
    //todo: add ability to change username for a user

}
