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

    @GetMapping("/id/{id}/exists") //중복 아이디 확인
    public ResponseEntity<Map> isExistsId(@PathVariable("id") String id) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", userService.isExistId(id));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/username/{username}/exists") //중복 닉네임 확인
    public ResponseEntity<Map> isExistsNickname(@PathVariable("username") String nickName) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", userService.isExistNickname(nickName));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    ///api/v1/users/referrers?referrer={추천인 아이디}
    @GetMapping("/referrers") //추천인 존재 확인
    public ResponseEntity<Map> isExistsRefferer(@RequestParam("referrer") String referrerId) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", userService.isExistId(referrerId));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
