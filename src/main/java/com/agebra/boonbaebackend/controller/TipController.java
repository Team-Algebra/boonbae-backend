package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.repository.TipRepository;
import com.agebra.boonbaebackend.service.TipService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@Tag(name = "TipController", description = "오늘의 꿀팁 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/tip")
public class TipController {

  private final TipService tipService;

  @GetMapping("/")
  public ResponseEntity getTip() {
    String tipRandom = tipService.getTip();

    Map<String, String> map = new HashMap<>();
    map.put("content", tipRandom);

    return ResponseEntity.ok(map);
  }

  @PostMapping("/")
  public ResponseEntity addTip(@RequestBody Map<String, String> map) {
    String tip = map.get("content");
    if (tip == null)
      throw new InputMismatchException("content 값은 필수입니다");

    tipService.addTip(tip);

    return ResponseEntity.ok().build();
  }

//  @DeleteMapping("/{tip_pk}") //관리자권한
//  public ResponseEntity deleteTip(@PathVariable("tip_pk") Long pk) {
//
//  }
//
//  @PatchMapping("/")
//  public ResponseEntity modifyTip() {
//
//  }
}
