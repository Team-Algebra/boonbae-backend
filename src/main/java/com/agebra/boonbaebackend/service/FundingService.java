package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Funding;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import com.agebra.boonbaebackend.dto.FundingDto;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.SecondCategoryRepository;
import com.agebra.boonbaebackend.repository.FundingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class FundingService {
  private final FundingRepository fundingRepository;
  private final SecondCategoryRepository secondCategoryRepository;

  public void addFunding(FundingDto.AddFunding dto, Users user) throws RuntimeException {
    //카테고리 올바른지 확인
    SecondCategory secondCategory = secondCategoryRepository.findById(dto.getSecond_category_pk())
      .orElseThrow(() -> new NotFoundException("해당하는 두 번째 카테고리가 존재하지 않음"));

    //펀딩 새로만들기
    Funding funding = Funding.builder()
      .user(user)
      .title(dto.getTitle())
      .category(secondCategory)
      .content(dto.getIntroduction())
      .targetAmount(dto.getTarget_amount())
      .supportingAmount(dto.getSupporting_amount())
      .openDate(dto.getOpen_date())
      .closeDate(dto.getClose_date())
      .mainImg(dto.getMain_image())
      .build();

      fundingRepository.save(funding);
  }
}
