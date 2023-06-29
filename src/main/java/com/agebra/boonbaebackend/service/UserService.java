package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void register(UserDto.RegisterRequest dto) {
        Users user = Users.makeUser(
          dto.getId(),
          passwordEncoder.encode(dto.getPassword()),
          dto.getUsername()
        );

        userRepository.save(user);
    }

    public UserDto.LoginResponse authenticate(UserDto.Login dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getId(),
                        dto.getPassword()
                )
        );

        Users user = userRepository.findById(dto.getId())
          .orElseThrow(() -> new NoSuchElementException());

        String jwtToken = jwtService.generateToken(user);
        return new UserDto.LoginResponse(jwtToken, user.getId(), user.getNickname());
    }

    //중복 아이디 확인
    @Transactional(readOnly = true)
    public boolean isExistId(String id) {
        Users user = userRepository.findById(id)
          .orElseGet(() -> null);

        return (user == null)? false : true;
    }

    //중복 닉네임 확인
    @Transactional(readOnly = true)
    public boolean isExistNickname(String nickName) {
        Users user = userRepository.findByNickname(nickName)
          .orElseGet(() -> null);

        return (user == null)? false : true;
    }

}
