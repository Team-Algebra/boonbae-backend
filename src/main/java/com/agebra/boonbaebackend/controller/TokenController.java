package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TokenController {

  private final UserService userService;

  @GetMapping("/token/validate")
  public ResponseEntity<Map> validateJwtToken(@RequestBody String request) {

    return ResponseEntity.ok().build();
  }

}
