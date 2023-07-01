package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.domain.Tag;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.RecyclingRepository;
import com.agebra.boonbaebackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RecyclingService {
  private final RecyclingRepository recyclingRepository;
  private final TagRepository tagRepository;

  // 분리배출 정보 등록
  @Transactional
  public void write(RecyclingDto.Write dto) {
    RecyclingInfo info = RecyclingInfo.makeRecyclingInfo(
      dto.getName(), dto.getType(), dto.getProcess(), dto.getDescription(), dto.getImage_url()
    );

    recyclingRepository.save(info);

    List<Tag> tagList = new ArrayList<>();

    for (String tag : dto.getTags()) {
      Tag existingTag = tagRepository.findByName(tag);
      if (existingTag == null) {
        existingTag = Tag.makeTag(info, tag);
        tagList.add(existingTag);
        tagRepository.save(existingTag);
      }
      info.addTagList(tagList);
    }

  }

  // 분리배출 정보 검색
  @Transactional(readOnly = true)
  public RecyclingDto.SearchResult searchRecyclingInfo(String keyword) {
    List<RecyclingInfo> infoList = recyclingRepository.findByKeyword(keyword);
    if (infoList.isEmpty()) {
      throw new NotFoundException("No recycling information found for the keyword: " + keyword);
    }

    List<RecyclingDto.Search> searchResults = infoList.stream().map(recyclingInfo -> {
      String[] tagNames = recyclingInfo.getTagList().stream()
        .map(Tag::getName)
        .toArray(String[]::new);

      return new RecyclingDto.Search(
        recyclingInfo.getPk(),
        recyclingInfo.getName(),
        recyclingInfo.getProcess(),
        recyclingInfo.getDescription(),
        recyclingInfo.getType(),
        recyclingInfo.getImageUrl(),
        tagNames,
        recyclingInfo.getViewCnt(),
        recyclingInfo.getCreateDate()
      );
    }).collect(Collectors.toList());

    return new RecyclingDto.SearchResult(searchResults.size(), searchResults);
  }




  // 특정 쓰레기 분리배출 정보 가져오기
  @Transactional(readOnly = true)
  public RecyclingDto.DetailResult getRecyclingInfoDetail(Long recyclePk) {
    RecyclingInfo recyclingInfo = recyclingRepository.findById(recyclePk)
      .orElseThrow(() -> new NotFoundException("Recycling information not found with PK: " + recyclePk));

    recyclingInfo.setViewCnt(recyclingInfo.getViewCnt() + 1); // view_cnt 1 증가
    recyclingRepository.save(recyclingInfo);

    String[] tagNames = recyclingInfo.getTagList().stream()
      .map(Tag::getName)
      .toArray(String[]::new);

    return new RecyclingDto.DetailResult(
      recyclingInfo.getName(),
      recyclingInfo.getProcess(),
      recyclingInfo.getDescription(),
      recyclingInfo.getType(),
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
