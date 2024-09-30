package com.nca.service;

import com.nca.dto.request.AuthenticationRequestDTO;
import com.nca.dto.response.AuthenticationResponseDTO;
import com.nca.entity.User;
import com.nca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;

    public AuthenticationResponseDTO requestAuthentication(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername()).get();

        String generateToken = jwtService.generateToken(request.getUsername(), Collections.singleton(user.getUserRole()));
        return AuthenticationResponseDTO.builder()
                .accessToken(generateToken)
                .refreshToken(generateToken)
                .build();
    }

}
