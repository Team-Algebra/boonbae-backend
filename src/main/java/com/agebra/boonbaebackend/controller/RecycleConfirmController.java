package com.agebra.boonbaebackend.controller;


import com.agebra.boonbaebackend.domain.RecycleConfirm;
import com.agebra.boonbaebackend.domain.RecycleConfirmStatus;
import com.agebra.boonbaebackend.dto.RecycleConfirmDto;
import com.agebra.boonbaebackend.service.RecycleConfirmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "RecycleConfirmController", description = "분리배출 인증사진 관련한 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/recycling_confirm")
public class RecycleConfirmController {

  private final RecycleConfirmService recycleConfirmService;

  @GetMapping("/")
  public ResponseEntity getAllRecycle(
    @RequestParam @Nullable RecycleConfirmStatus status,
    @PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    RecycleConfirmDto.Info list = recycleConfirmService.findAll(status, pageable);

    return ResponseEntity.ok(list);
  }




}
