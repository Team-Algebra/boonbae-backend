package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.exception.UserInfoDuplicatedException;
import com.agebra.boonbaebackend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "UserController", description = "User관련한 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/") //회원가입
    public ResponseEntity<UserDto.RegisterResponse> register (@RequestBody @Valid UserDto.RegisterRequest request) throws RuntimeException {
        userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
          UserDto.RegisterResponse
            .builder()
            .id(request.getId())
            .username(request.getUsername())
            .build()
        );
    }

    @PostMapping("/login") //로그인
    public ResponseEntity<UserDto.LoginResponse> authenticate(
      @RequestBody @Valid UserDto.Login request
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

    @DeleteMapping("/")
    public ResponseEntity deleteUser(@AuthenticationPrincipal Users user) {
        userService.delete(user);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/username")
    public ResponseEntity modifyNickname(@AuthenticationPrincipal Users user, @RequestBody Map<String, String> map) throws UserInfoDuplicatedException {
        String userName = map.get("username");
        if (userName == null)
            throw new InputMismatchException();

        userService.modifyUsername(user, userName);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/info/my")
    public ResponseEntity getUserInfo(@AuthenticationPrincipal Users user) {

        UserDto.Info userInfo = userService.getUserInfo(user);

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/introduction")
    public ResponseEntity getIntroduction(@AuthenticationPrincipal Users user) {
        String introduction = userService.getIntroduction(user);
        Map<String, String> map = new HashMap<>();
        map.put("introduction", introduction);

        return ResponseEntity.ok(map);
    }

    @PatchMapping("/introduction")
    public ResponseEntity modifyIntroduction(@AuthenticationPrincipal Users user, @RequestBody Map<String, String> map) throws InputMismatchException{
        String introduction = map.get("introduction");
        if (introduction == null)
            throw new InputMismatchException("introduction이 비어있습니다");

        userService.modifyIntroduction(user, introduction);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity modifyPassword(@AuthenticationPrincipal Users user, @RequestBody @Valid UserDto.modifyPassword dto) throws InputMismatchException{
        userService.modifyPassword(user,dto);

        return ResponseEntity.ok().build();
    }
}
