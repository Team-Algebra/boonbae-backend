package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.service.JwtService;
import com.agebra.boonbaebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TokenController {

  private final UserService userService;
  private final JwtService jwtService;

  @GetMapping("/token/validate")
  public ResponseEntity<Map<String, Object>> validateJwtToken(@AuthenticationPrincipal Users user) {
    Map<String, Object> response = new HashMap<>();

    boolean isValid = jwtService.isTokenValidByUsers(user);


    Map<String, Object> userResponse = new HashMap<>();
    if (isValid) {
      userResponse.put("id", user.getId());
      userResponse.put("username", user.getNickname());
    }

    response.put("isValid", isValid);
    response.put("user", userResponse);

    return ResponseEntity.ok(response);
  }

}
