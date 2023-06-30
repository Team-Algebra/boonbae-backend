package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.*;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.dto.TreeDto;
import com.agebra.boonbaebackend.exception.NoSuchUserException;
import com.agebra.boonbaebackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class TreeService {
  private final UserRepository userRepository;
  private final TreeRepository treeRepository;
  private final RecycleConfirmRepository recycleConfirmRepository;

  public Long getAllExp() {
    Long sum = treeRepository.getAllExp().orElseGet(() -> 0L);

    return sum;
  }

  public void uploadRecycle(Users user, TreeDto.Confirm dto) {
    Users findUsers = userRepository.findById(user.getPk())
      .orElseThrow(() -> new RuntimeException());

    RecycleConfirm recycleConfirm = RecycleConfirm.builder()
      .user(findUsers)
      .imageUrl(dto.getImage_url())
      .build();

    recycleConfirmRepository.save(recycleConfirm);
  }

  public TreeDto.Info getUsersTreeInfo(Long userPk) throws RuntimeException{
    Users findUser = userRepository.findById(userPk)
      .orElseGet(() -> null);

    if (findUser == null)
      throw new NoSuchUserException("해당하는 user가 없습니다");

    Tree tree = findUser.getTree();
    Long allUserCnt = userRepository.countBy();
    Long userRank = treeRepository.findRankById(tree.getPk());

    TreeDto.Info info = TreeDto.Info.builder()
      .current_exp(tree.getExp())
      .accumulated_exp(tree.getAccumulatedExp())
      .all_cnt(allUserCnt)
      .rank(userRank)
      .recycle_cnt(tree.getRecycleCnt())
      .build();

    return info;
  }

  public TreeDto.Info getMyTreeInfo(Users user) {
    Users findUser = userRepository.findById(user.getPk())
      .orElseGet(() -> null);

    if (findUser == null)
      throw new NoSuchUserException("해당하는 user가 없습니다");

    Tree tree = findUser.getTree();
    tree.init(); //날짜가 지났는지 확인 후 초기화

    Long allUserCnt = userRepository.countBy();
    Long myrank = treeRepository.findRankById(tree.getPk());

    TreeDto.Info info = TreeDto.Info.builder()
      .current_exp(tree.getExp())
      .accumulated_exp(tree.getAccumulatedExp())
      .all_cnt(allUserCnt)
      .rank(myrank)
      .recycle_cnt(tree.getRecycleCnt())
      .is_watching_ad(tree.isWatchingAd())
      .tonic_available(tree.getTonicAvailable())
      .eco_point(findUser.getEcoPoint())
      .build();

    return info;
  }



}
