package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.Map;

@Tag(name = "MiscController", description = "기타 기능, 동작을 처리하는 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class MiscController {

  private final UserService userService;

  @PostMapping("/point")
  public ResponseEntity chargePoint(@AuthenticationPrincipal Users user, @RequestBody Map<String, Integer> map) {
    Integer amount = map.get("amount");

    if (amount == null)
      throw new InputMismatchException();

    userService.chargePoint(user, amount);

    return ResponseEntity.ok().build();
  }



}
