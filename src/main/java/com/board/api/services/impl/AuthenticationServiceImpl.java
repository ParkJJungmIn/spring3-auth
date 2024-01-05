package com.board.api.services.impl;

import com.board.api.confiq.JwtService;
import com.board.api.entities.LoginRequest;
import com.board.api.entities.Role;
import com.board.api.entities.User;
import com.board.api.exception.AuthException;
import com.board.api.payloads.AuthenticationResponse;
import com.board.api.repositories.UserRepo;
import com.board.api.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
//    private final

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        AuthenticationResponse response = new AuthenticationResponse();
        Optional<User> userDetails = repository.findByEmail(request.getEmail());
        if(userDetails.isEmpty()){
            throw new AuthException("Email not found");
        }else{
            if( !passwordEncoder.matches(request.getPassword(), userDetails.get().getPassword() ) ){
                throw new AuthException("password do not match");
            }
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( request.getEmail(), request.getPassword() ));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        response.setToken(jwtToken);
        response.setName(userDetails.get().getName());
        return response;
    }

    @Override
    public AuthenticationResponse register(User request) {
        Optional<User> userDetails = repository.findByEmail(request.getEmail());
        if(userDetails.isPresent()){
            throw new AuthException("Email 존재");
        }

        var user =User.builder()
                .name(request.getName())
                .about(request.getAbout())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setMessage("Successful");
        response.setToken(jwtToken);
        return response;

    }
}
