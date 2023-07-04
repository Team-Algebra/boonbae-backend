package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.dto.TipDto;
import com.agebra.boonbaebackend.service.TipService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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

  @Secured("ADMIN") //관리자권한 필요
  @GetMapping("/all")
  public ResponseEntity getAllTip() {
    List<TipDto.List> tipList = tipService.getAll();

    return ResponseEntity.ok(tipList);
  }

  @Secured("ADMIN") //관리자권한 필요
  @PostMapping("/")
  public ResponseEntity addTip(@RequestBody Map<String, String> map) {
    String tip = map.get("content");
    if (tip == null)
      throw new InputMismatchException("content 값은 필수입니다");

    tipService.addTip(tip);

    return ResponseEntity.ok().build();
  }

  @Secured("ADMIN") //관리자권한 필요
  @DeleteMapping("/{tip_pk}")
  public ResponseEntity deleteTip(@PathVariable("tip_pk") Long pk) {
    tipService.deleteTip(pk);

    return ResponseEntity.ok().build();
  }

  @Secured("ADMIN") //관리자권한 필요
  @PatchMapping("/{tip_pk}")
  public ResponseEntity modifyTip(@PathVariable("tip_pk") Long pk, @RequestBody Map<String, String> map) {
    String content = map.get("content");
    if (content == null)
      throw new InputMismatchException("content 값은 필수입니다");

    tipService.modifyTip(pk, content);

    return ResponseEntity.ok().build();
  }
}
