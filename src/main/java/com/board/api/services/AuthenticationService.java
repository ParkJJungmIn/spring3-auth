package com.board.api.services;


import com.board.api.entities.LoginRequest;
import com.board.api.entities.User;
import com.board.api.payloads.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest user);

    AuthenticationResponse register(User user);

}
