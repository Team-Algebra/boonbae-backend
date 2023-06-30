package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.service.RecyclingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RecyclingInfoController", description = "분리배출정보 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/recycling")
public class RecyclingInfoController {

  private final RecyclingService recyclingService;

  @PostMapping("/") //관리자용
  public ResponseEntity write(@RequestBody @Valid RecyclingDto.Write dto) {
    recyclingService.write(dto);

    return ResponseEntity.ok().build();
  }

  // 분리배출 정보 검색
  @GetMapping
  public ResponseEntity<RecyclingDto.SearchResult> searchRecyclingInfo(@RequestParam("q") String query) {
    RecyclingDto.SearchResult result = recyclingService.searchRecyclingInfo(query);
    return ResponseEntity.ok(result);
  }




}
