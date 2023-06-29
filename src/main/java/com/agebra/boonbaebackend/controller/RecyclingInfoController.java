package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.service.RecyclingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
