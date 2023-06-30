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

  public void write(RecyclingDto.Write dto) {
    RecyclingInfo info = RecyclingInfo.makeRecyclingInfo(
      dto.getName(), dto.getType(), dto.getProcess(), dto.getDescription(), dto.getImage_url()
    );

    recyclingRepository.save(info);

    List<Tag> tagList = new ArrayList<>();
    for (String tag: dto.getTags()) {
      Tag newTag = Tag.makeTag(info, tag);
      tagList.add(newTag);
      tagRepository.save(newTag);
    }

    info.addTagList(tagList);
  }

  // 분리배출 정보 검색
  public RecyclingDto.SearchResult searchRecyclingInfo(String criteria) {
    String keyword = criteria;

    List<RecyclingInfo> infoList = recyclingRepository.findByKeyword(keyword); // 키워드를 이용한 검색 쿼리 실행

    int count = infoList.size();

    return new RecyclingDto.SearchResult(count, infoList);
  }

}
