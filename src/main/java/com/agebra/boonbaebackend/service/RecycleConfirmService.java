package com.agebra.boonbaebackend.service;


import com.agebra.boonbaebackend.domain.RecycleConfirm;
import com.agebra.boonbaebackend.domain.RecycleConfirmStatus;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.RecycleConfirmDto;
import com.agebra.boonbaebackend.repository.RecycleConfirmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class RecycleConfirmService {
  private final RecycleConfirmRepository recycleConfirmRepository;


  public RecycleConfirmDto.Info findAll(RecycleConfirmStatus status, Pageable pageable) {
    Page<RecycleConfirm> pageList;

    if (status == null)
      pageList = recycleConfirmRepository.findAll(pageable);
    else
      pageList = recycleConfirmRepository.findByStatus(status, pageable);

    int totalPages = pageList.getTotalPages();

    List<RecycleConfirm> list = pageList.getContent();
    RecycleConfirmDto.Info info = convertToDto(totalPages, list);

    return info;
  }

  public RecycleConfirmDto.Info convertToDto(int totalPages, List<RecycleConfirm> list) {
    List<RecycleConfirmDto.Info.Detail> detailList = new ArrayList<>();

    for (RecycleConfirm rc: list) {
      Users user = rc.getUser();
      RecycleConfirmDto.Info.Detail detail = new RecycleConfirmDto.Info.Detail(
        rc.getPk(), user.getPk(), user.getUsername(), rc.getStatus()
      );

      detailList.add(detail);
    }

    return new RecycleConfirmDto.Info(totalPages, detailList);
  }

  private void changeStatus(Long recyclingConfirmPk, RecycleConfirmStatus status) {
    RecycleConfirm recycleConfirm = recycleConfirmRepository.findById(recyclingConfirmPk)
      .orElseThrow(() -> new NoSuchElementException("해당하는 분리배출 인증사진이 없습니다"));

    recycleConfirm.pass();
  }

  public void pass(Long recyclingConfirmPk) {
    changeStatus(recyclingConfirmPk, RecycleConfirmStatus.PASS);
  }

  public void unpass(Long recyclingConfirmPk) {
    changeStatus(recyclingConfirmPk, RecycleConfirmStatus.UNPASS);
  }
}
