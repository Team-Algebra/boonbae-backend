package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Tip;
import com.agebra.boonbaebackend.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TipService {

  private final TipRepository tipRepository;

  @Transactional(readOnly = true)
  public String getTip() {
    return tipRepository.getTipRandom()
      .orElseGet(() -> "");
  }

  public void addTip(String tip) {
    Tip newTIp = Tip.builder()
      .content(tip)
      .build();

    tipRepository.save(newTIp);
  }

}
