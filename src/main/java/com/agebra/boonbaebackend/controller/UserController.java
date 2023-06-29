package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "UserController", description = "User관련한 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/") //회원가입
    public ResponseEntity<UserDto.RegisterResponse> register(
      @RequestBody UserDto.RegisterRequest request
    ) {
        userService.register(request);
        return ResponseEntity.ok(
          UserDto.RegisterResponse.builder().id(request.getId()).username(request.getUsername()).build()
        );
    }

    @PostMapping("/login") //로그인
    public ResponseEntity<UserDto.LoginResponse> authenticate(
      @RequestBody UserDto.Login request
    ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @GetMapping("/id/{id}/exists")
    public ResponseEntity<Map> isExistsId(@PathVariable("id") String id) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", userService.isExistId(id));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
