package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "User관련한 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto.RegisterResponse> register(
      @RequestBody UserDto.RegisterRequest request
    ) {
        userService.register(request);
        return ResponseEntity.ok(
          UserDto.RegisterResponse.builder().id(request.getId()).username(request.getUsername()).build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> authenticate(
      @RequestBody UserDto.Login request
    ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
