package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.service.RecyclingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "RecyclingInfoController", description = "분리배출정보 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/recycling")
public class RecyclingInfoController {

  private final RecyclingService recyclingService;

  @Secured("ADMIN") //관리자권한 필요
  @PostMapping //분리배출 정보 등록
  public ResponseEntity write(@RequestBody @Valid RecyclingDto.Write dto) {
    recyclingService.write(dto);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  // 분리배출 정보 검색
  @GetMapping("/")
  public ResponseEntity<RecyclingDto.SearchResult> searchRecyclingInfo(@RequestParam("q") String keyword) {
    RecyclingDto.SearchResult dto = recyclingService.searchRecyclingInfo(keyword);
    return ResponseEntity.ok(dto);
  }

  // 특정 쓰레기 분리배출 정보 가져오기
  @GetMapping("/{recycle_pk}")
  public ResponseEntity<RecyclingDto.DetailResult> getRecyclingInfoDetail(@PathVariable("recycle_pk") Long recyclePk) {
    RecyclingDto.DetailResult dto = recyclingService.getRecyclingInfoDetail(recyclePk);
    return ResponseEntity.ok(dto);
  }

}
