package com.agebra.boonbaebackend.service;


import com.agebra.boonbaebackend.domain.RecycleConfirm;
import com.agebra.boonbaebackend.domain.RecycleConfirmStatus;
import com.agebra.boonbaebackend.domain.Tree;
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

@RequiredArgsConstructor
@Transactional
@Service
public class RecycleConfirmService {
  private final RecycleConfirmRepository recycleConfirmRepository;
  private final ValueService valueService;

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

  public void pass(Long recyclingConfirmPk) {
    RecycleConfirm recycleConfirm = recycleConfirmRepository.findById(recyclingConfirmPk)
      .orElseThrow(() -> new NoSuchElementException("해당하는 분리배출 인증사진이 없습니다"));

    //나무 경험치 지급
    Tree tree = recycleConfirm.getUser().getTree();
    tree.recyclePass(valueService.getRecycleExp());

    //분리배출 인증완료 혜택지급
    Users user = recycleConfirm.getUser();
    user.addRecyclePoint(valueService.getRecyclePoint());

    //분리배출 인증사진 인증완료 처리
    recycleConfirm.pass();
  }

  public void unpass(Long recyclingConfirmPk) {
    RecycleConfirm recycleConfirm = recycleConfirmRepository.findById(recyclingConfirmPk)
      .orElseThrow(() -> new NoSuchElementException("해당하는 분리배출 인증사진이 없습니다"));

    //분리배출 인증사진 인증실패 처리
    recycleConfirm.unpass();
  }
}
