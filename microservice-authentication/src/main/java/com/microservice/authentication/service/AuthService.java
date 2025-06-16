package com.microservice.authentication.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.microservice.authentication.DTO.LoginRequest;
import com.microservice.authentication.DTO.LoginResponse;
import com.microservice.authentication.DTO.RegisterRequest;
import com.microservice.authentication.DTO.UserDTO;
import com.microservice.authentication.entity.Permission;
import com.microservice.authentication.entity.Role;
import com.microservice.authentication.entity.User;
import com.microservice.authentication.mapper.UserMapper;
import com.microservice.authentication.repository.RoleRepository;
import com.microservice.authentication.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;

@Service
public class AuthService {
    @Autowired
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    @Value("${jwt.key}")
    private String jwtKey;
    @Value("${jwt.durationMinutes}")
    private int durationMinutes;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public LoginResponse login (LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
            .orElse(null);

        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getHashedPassword())) {
            return null;
        }

        String token = generateToken(user);

        return new LoginResponse(user.getEmail(), token);
    }

    public String generateToken (User user) {  
        byte[] signingKey = jwtKey.getBytes(StandardCharsets.UTF_8);

        List<String> permissions = user.getRole().getPermissions().stream()
            .map(Permission::getPermissionName)
            .collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUserId().toString());
        claims.put("name", user.getUserName());
        claims.put("role", user.getRole().getRoleName());
        claims.put("permissions", permissions);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + durationMinutes * 60 * 1000);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, signingKey)
            .compact();
    }

    @Transactional
    public UserDTO register (RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

       Role role = roleRepository.findById(request.getRoleId())  
        .orElseThrow(() -> new IllegalArgumentException("Role does not exist"));
        
        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setHashedPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setRole(role);
        
        userRepository.save(user);
        return userMapper.toDTO(user);
    }
}
