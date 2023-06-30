package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.domain.Tag;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.repository.RecyclingRepository;
import com.agebra.boonbaebackend.repository.TagRepository;
import com.agebra.boonbaebackend.repository.TreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TreeService {
  private final TreeRepository treeRepository;

  public Long getAllExp() {
    Long sum = treeRepository.getAllExp().orElseGet(() -> 0L);

    return sum;
  }



}
