package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Tip;
import com.agebra.boonbaebackend.exception.NotFoundException;
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

  public void deleteTip(Long pk) {
    Tip findTip = tipRepository.findById(pk)
      .orElseThrow(() -> new NotFoundException("해당하는 tip이 없습니다"));

    tipRepository.delete(findTip);
  }

  public void modifyTip(Long pk, String content) {
    Tip findTip = tipRepository.findById(pk)
      .orElseThrow(() -> new NotFoundException("해당하는 tip이 없습니다"));

    findTip.modifyContent(content);
  }

}
