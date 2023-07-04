package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.*;
import com.agebra.boonbaebackend.dto.TreeDto;
import com.agebra.boonbaebackend.exception.NoSuchUserException;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.exception.UploadCntExceedException;
import com.agebra.boonbaebackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TreeService {
  private final UserRepository userRepository;
  private final TreeRepository treeRepository;
  private final RecycleConfirmRepository recycleConfirmRepository;
  private final ValueService valueService;

  public Long getAllExp() {
    return treeRepository.getAllExp().orElseGet(() -> 0L);
  }

  public RecycleConfirm uploadRecycle(Users user, TreeDto.Confirm dto) {
    Users findUser = userRepository.findByPkWithTree(user.getPk())
      .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

    Tree tree = findUser.getTree();
    tree.initAll();

    if (tree.isExceedUploadCnt()) {
      throw new UploadCntExceedException("분리수거 인증가능 횟수를 초과하였습니다");
    }

    tree.recycleComplete();

    RecycleConfirm recycleConfirm = RecycleConfirm.builder()
      .user(findUser)
      .imageUrl(dto.getImage_url())
      .build();

    RecycleConfirm save = recycleConfirmRepository.save(recycleConfirm);

    return save;
  }

  public TreeDto.Info getUsersTreeInfo(Long userPk) {
    Users findUser = userRepository.findByPkWithTree(userPk)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

    Tree tree = findUser.getTree();
    tree.initAll();

    Long allUserCnt = userRepository.countBy();
    Long userRank = treeRepository.findRankByExp(tree.getExp());

    TreeDto.Info info = TreeDto.Info.builder()
      .current_exp(tree.getExp())
      .all_cnt(allUserCnt)
      .rank(userRank)
      .recycle_cnt(tree.getRecycleCnt())
      .build();

    return info;
  }

  public TreeDto.Info getMyTreeInfo(Users user) {
    Users findUser = userRepository.findByPkWithTree(user.getPk())
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

    Tree tree = findUser.getTree();
    tree.initAll();

    Long allUserCnt = userRepository.countBy();
    Long myrank = treeRepository.findRankByExp(tree.getExp());

    return TreeDto.Info.builder()
      .current_exp(tree.getExp())
      .all_cnt(allUserCnt)
      .upload_available(tree.getUploadAvailable())
      .rank(myrank)
      .recycle_cnt(tree.getRecycleCnt())
      .is_watching_ad(tree.isWatchingAd())
      .tonic_available(tree.getTonicAvailable())
      .eco_point(findUser.getEcoPoint())
      .build();
  }

  public TreeDto.Tier getTier(Users user) {
    Users findUser = userRepository.findByPkWithTree(user.getPk())
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

    Tree tree = findUser.getTree();
    tree.initAll(); //날짜가 지났는지 확인 후 초기화

    Long allUserCnt = userRepository.countBy();
    Long myrank = treeRepository.findRankByExp(tree.getExp());

    return TreeDto.Tier.builder()
      .all_cnt(allUserCnt)
      .rank(myrank)
      .build();
  }

  public TreeDto.CommonResponseDTO confirmAdvertisementView(Users user) {
    Users findUser = userRepository.findByPkWithTree(user.getPk())
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

    Tree tree = findUser.getTree();
    tree.initAll();

    if (tree.isExceedWatchingAd()) {
      return new TreeDto.CommonResponseDTO(false, "EXCEED");
    }

    // 광고 시청 가능 시 아래 로직 수행
    tree.watchAd(valueService.getAdvertisementExp());
    return new TreeDto.CommonResponseDTO(true, null);
  }

  public TreeDto.CommonResponseDTO useTonic(Users user, int amount) {
    Users findUser = userRepository.findByPkWithTree(user.getPk())
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

    Tree tree = findUser.getTree();
    tree.initAll();

    if (tree.isExceedTonicCnt(amount)) {
      return new TreeDto.CommonResponseDTO(false, "EXCEED");
    }

    if (findUser.isExceedPoint(amount, valueService.getTonicPoint())) {
      return new TreeDto.CommonResponseDTO(false, "LACK_POINT");
    }

    // 토닉 구매 및 사용 가능 시 아래 로직 수행
    tree.useTonic(amount, valueService.getTonicExp());
    findUser.useTonic(amount, valueService.getTonicPoint());
    return new TreeDto.CommonResponseDTO(true, null);
  }
}
