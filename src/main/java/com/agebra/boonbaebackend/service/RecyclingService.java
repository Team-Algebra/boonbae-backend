package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.domain.Tag;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.repository.RecyclingRepository;
import com.agebra.boonbaebackend.repository.TagRepository;
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
  private final TagRepository tagRepository;

  // 분리배출 정보 등록
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
  public RecyclingDto.SearchResult searchRecyclingInfo(String keyword) {

    List<RecyclingInfo> infoList = recyclingRepository.findByKeyword(keyword); // 키워드를 이용한 검색 쿼리 실행

    int count = infoList.size();

    return new RecyclingDto.SearchResult(count, infoList);
  }

  // 특정 쓰레기 분리배출 정보 가져오기
  public RecyclingInfo getRecyclingInfoDetail(Long recyclePk) {
    recyclingRepository.updateViewCount(recyclePk); // view_cnt 1증가
    return recyclingRepository.findById(recyclePk).orElse(null);
  }



}
