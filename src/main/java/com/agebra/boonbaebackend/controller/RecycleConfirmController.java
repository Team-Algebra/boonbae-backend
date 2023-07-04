package com.agebra.boonbaebackend.controller;


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
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "RecycleConfirmController", description = "분리배출 인증사진 관련한 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/recycling_confirm") //관리자 전용 url
public class RecycleConfirmController {

  private final RecycleConfirmService recycleConfirmService;

  @GetMapping("/")
  public ResponseEntity getAllRecycle(
    @RequestParam @Nullable RecycleConfirmStatus status,
    @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    RecycleConfirmDto.Info list = recycleConfirmService.findAll(status, pageable);

    return ResponseEntity.ok(list);
  }

  @PatchMapping("/{recycling_confirm_pk}/pass") //관리자용
  public ResponseEntity passRecycle(@RequestParam("recycling_confirm_pk") Long recyclingConfirmPk) {
    recycleConfirmService.pass(recyclingConfirmPk);

    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{recycling_confirm_pk}/unpass") //관리자용
  public ResponseEntity unpassRecycle(@RequestParam("recycling_confirm_pk") Long recyclingConfirmPk) {
    recycleConfirmService.unpass(recyclingConfirmPk);

    return ResponseEntity.ok().build();
  }

}
