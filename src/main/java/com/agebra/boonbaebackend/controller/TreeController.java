package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.TreeDto;
import com.agebra.boonbaebackend.service.TreeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Tag(name = "TreeController", description = "Tree관련한 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/trees")
public class TreeController {

  private final TreeService treeService;

  @GetMapping("/")
  public ResponseEntity treeGrowth() {
    Long exp = treeService.getAllExp();

    Map<String ,Long> map = new HashMap<>();
    map.put("exp", exp);

    return ResponseEntity.ok(map);
  }

  //인증사진 업로드하고 포인트받기
  @PostMapping("/")
  public ResponseEntity uploadRecycle(@AuthenticationPrincipal Users user, @RequestBody TreeDto.Confirm dto) {
    treeService.uploadRecycle(user, dto);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/my")
  public ResponseEntity getMyTreeInfo(@AuthenticationPrincipal Users user) {

    TreeDto.Info myTreeInfo = treeService.getMyTreeInfo(user);

    return ResponseEntity.ok(myTreeInfo);
  }

  @GetMapping("/{user_pk}")
  public ResponseEntity getUsersTreeInfo( @PathVariable("user_pk") Long userPk) {

    TreeDto.Info usersTreeInfo = treeService.getUsersTreeInfo(userPk);

    return ResponseEntity.ok(usersTreeInfo);
  }




}
