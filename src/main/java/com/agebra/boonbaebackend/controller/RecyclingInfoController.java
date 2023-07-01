package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
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

  @PostMapping("/") //관리자용. 분리배출 정보 등록
  public ResponseEntity write(@RequestBody @Valid RecyclingDto.Write dto) {
    recyclingService.write(dto);

    return ResponseEntity.ok().build();
  }

  // 분리배출 정보 검색
  @GetMapping
  public ResponseEntity<RecyclingDto.SearchResult> searchRecyclingInfo(@RequestParam("q") String keyword) {
    RecyclingDto.SearchResult result = recyclingService.searchRecyclingInfo(keyword);
    return ResponseEntity.ok(result);
  }
  
  // 특정 쓰레기 분리배출 정보
  @GetMapping("/{recycle_pk}")
  public ResponseEntity<RecyclingDto.DetailResult> getRecyclingInfo(@PathVariable("recycle_pk") Long recyclePk) {
    RecyclingInfo recyclingInfo = recyclingService.getRecyclingInfoDetail(recyclePk);

    if (recyclingInfo == null) {
      // 해당 쓰레기 분리배출 정보가 없을 경우 예외 처리
      return ResponseEntity.notFound().build();
    }

    RecyclingDto.DetailResult dto = new RecyclingDto.DetailResult(
            recyclingInfo.getName(),
            recyclingInfo.getProcess(),
            recyclingInfo.getDescription(),
            recyclingInfo.getType(),
            recyclingInfo.getImageUrl(),
            recyclingInfo.getTagList(),
            recyclingInfo.getViewCnt(),
            recyclingInfo.getCreateDate()
    );

    return ResponseEntity.ok(dto);
  }




}
