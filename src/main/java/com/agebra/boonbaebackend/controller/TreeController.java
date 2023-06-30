package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.service.TreeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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





}
