package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.*;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.exception.RecyclingInfoDuplicateException;
import com.agebra.boonbaebackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RecyclingService {
  private final RecyclingRepository recyclingRepository;
  private final RecyclingTypeRepository recyclingTypeRepository;
  private final RecyclingInfoTypeRepository recyclingInfoTypeRepository;
  private final TagRepository tagRepository;
  private final RecyclingInfoTagRepository recyclingInfoTagRepository;

  // 분리배출 정보 등록
  @Transactional
  public void write(RecyclingDto.Write dto) {
    //버리는법 리스트 -> String통으로 변경
    String processString = String.join("\t\t\t", dto.getProcess());

    //상세설명 리스트 -> String통으로 변경
    String discriptionString = String.join("\t\t\t", dto.getDescription());

    RecyclingInfo info = RecyclingInfo.makeRecyclingInfo(
      dto.getName(), processString, discriptionString, dto.getImage_url()
    );

    if(isExistInfoName(dto.getName()))
      throw new RecyclingInfoDuplicateException("분리배출 정보의 이름이 중복됩니다.");

    // 게시글 저장
    RecyclingInfo saveInfo = recyclingRepository.save(info);

    // type 매핑 -> 일치 type 없을 시 404
    if (dto.getTypes() != null) {
      for (String recyclingType  : dto.getTypes()) {
        List<RecyclingType> existingRecyclingTypeList = recyclingTypeRepository.findByName(recyclingType)
          .orElseThrow(() -> new NotFoundException("RecyclingType not found with name: " + recyclingType));

        if (existingRecyclingTypeList.size() == 0)
          throw new NotFoundException("RecyclingType not found with name: " + recyclingType);

        RecyclingType existingRecyclingType = existingRecyclingTypeList.get(0);

        RecyclingInfoType recyclingInfoType = RecyclingInfoType.builder()
          .recyclingInfo(saveInfo)
          .type(existingRecyclingType)
          .build();

        recyclingInfoTypeRepository.save(recyclingInfoType);
      }
    }

    // tag 매핑 -> 일치 tag 없을 시 생성 후 저장
    for (String tag : dto.getTags()) {
      List<Tag> existingTagList = tagRepository.findByName(tag);

      Tag existingTag = null;

      if (existingTagList.size() != 0)
        existingTag = existingTagList.get(0);

      if (existingTag == null) {
        Tag newTag = Tag.builder()
          .name(tag)
          .build();

        existingTag = tagRepository.save(newTag);
      }

      RecyclingInfoTag recyclingInfoTag = RecyclingInfoTag.builder()
              .recyclingInfo(saveInfo)
              .tag(existingTag)
              .build();

      recyclingInfoTagRepository.save(recyclingInfoTag);
    }
  }


  //중복 분리배출 정보 이름 확인
  @Transactional(readOnly = true)
  public boolean isExistInfoName(String infoName) {
    List<RecyclingInfo> recyclingInfoList = recyclingRepository.findByName(infoName)
            .orElseGet(() -> new ArrayList<>());

    return (recyclingInfoList.size() == 0)? false : true;
  }

  // 분리배출 정보 검색
  @Transactional(readOnly = true)
  public RecyclingDto.SearchResult searchRecyclingInfo(String keyword) {
    List<RecyclingInfo> infoList = recyclingRepository.findByKeyword(keyword);

//    if (infoList.isEmpty()) {
//      throw new NotFoundException("No recycling information found for the keyword: " + keyword);
//    }

    List<RecyclingDto.Search> searchResults = infoList.stream().map(recyclingInfo -> {

      // tag 추합
      String[] tagNames = recyclingInfo.getRecycleTagList().stream()
        .map(RecyclingInfoTag::getTag)
        .map(Tag::getName)
        .toArray(String[]::new);

      // type 추합
      String[] typeNames = recyclingInfo.getRecycleTypeList().stream()
        .map(RecyclingInfoType::getType)
        .map(RecyclingType::getName)
        .toArray(String[]::new);

      return new RecyclingDto.Search(
        recyclingInfo.getPk(),
        recyclingInfo.getName(),
        recyclingInfo.getProcess().split("\t\t\t"),
        recyclingInfo.getDescription().split("\t\t\t"),
        typeNames,
        recyclingInfo.getImageUrl(),
        tagNames,
        recyclingInfo.getViewCnt(),
        recyclingInfo.getCreateDate()
      );
    }).toList();

    return new RecyclingDto.SearchResult(searchResults.size(), searchResults);
  }


  // 특정 쓰레기 분리배출 정보 가져오기
  @Transactional
  public RecyclingDto.DetailResult getRecyclingInfoDetail(Long recyclePk) {
    RecyclingInfo recyclingInfo = recyclingRepository.findById(recyclePk)
      .orElseThrow(() -> new NotFoundException("Recycling information not found with PK: " + recyclePk));

    recyclingInfo.setViewCnt(recyclingInfo.getViewCnt() + 1); // view_cnt 1 증가
    recyclingRepository.save(recyclingInfo);

    // tag 추합
    String[] tagNames = recyclingInfo.getRecycleTagList().stream()
      .map(RecyclingInfoTag::getTag)
      .map(Tag::getName)
      .toArray(String[]::new);

    // type 추합
    String[] typeNames = recyclingInfo.getRecycleTypeList().stream()
      .map(RecyclingInfoType::getType)
      .map(RecyclingType::getName)
      .toArray(String[]::new);

    return new RecyclingDto.DetailResult(
      recyclingInfo.getName(),
            recyclingInfo.getProcess().split("\t\t\t"),
            recyclingInfo.getDescription().split("\t\t\t"),
      typeNames,
      recyclingInfo.getImageUrl(),
      tagNames,
      recyclingInfo.getViewCnt(),
      recyclingInfo.getCreateDate()
    );
  }

  public List<String> getRankFive() {

    List<String> list = new ArrayList<>();
    for (RecyclingInfo info: recyclingRepository.findTop5ByOrderByViewCntDesc()) {
      list.add(info.getName());
    }

    return list;
  }

}
