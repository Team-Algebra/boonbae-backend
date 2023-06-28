package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.AuthenticationResponse;
import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(UserDto.Register dto) {
        Users user = Users.makeUser(
          dto.getId(),
          passwordEncoder.encode(dto.getPassword()),
          dto.getUsername()
        );

        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(UserDto.Login dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getId(),
                        dto.getPassword()
                )
        );

        Users user = userRepository.findById(dto.getId())
          .orElseThrow(() -> new NoSuchElementException());

        log.info("@@@");

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, user.getId(), user.getUsername());
    }
}
