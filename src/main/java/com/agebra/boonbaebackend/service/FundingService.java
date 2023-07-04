package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.*;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import com.agebra.boonbaebackend.dto.FundingDonateDto;
import com.agebra.boonbaebackend.dto.FundingDto;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.FundingDonateRepository;
import com.agebra.boonbaebackend.repository.FundingLikeRepository;
import com.agebra.boonbaebackend.repository.SecondCategoryRepository;
import com.agebra.boonbaebackend.repository.FundingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class FundingService {
  private final FundingRepository fundingRepository;
  private final SecondCategoryRepository secondCategoryRepository;
  private final FundingLikeRepository fundingLikeRepository;
  private final FundingDonateRepository fundingDonateRepository;
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

  @Transactional
  public void addLikeToFunding(Users user, Long fundingPk) {
    Funding funding = fundingRepository.findById(fundingPk)
            .orElseThrow(() -> new NotFoundException("Funding not found with PK: " + fundingPk));

    // 이미 좋아요를 눌렀는지 확인
    boolean hasLiked = fundingLikeRepository.existsByUserAndFunding(user, funding);

    if (!hasLiked) {
      fundingLikeRepository.save(FundingLike.builder().user(user).funding(funding).build());
    }
  }


  @Transactional
  public void removeLikeFromFunding(Users user, Long fundingPk) {
    Funding funding = fundingRepository.findById(fundingPk)
            .orElseThrow(() -> new NotFoundException("Funding not found with PK: " + fundingPk));

    FundingLike like = fundingLikeRepository.findByUserAndFunding(user, funding)
            .orElseThrow(() -> new NotFoundException("Like not found for the given user and funding"));

    fundingLikeRepository.delete(like);
  }

  public boolean paymentCheck(FundingDonateDto.Request_All requestDto, PaymentMethod paymentMethod){ //결제 확인(미완)
    return true;
  }


  @Transactional
  public void addDonateToFunding(Users user, Long fundingPk){   //펀딩 후원
    Funding funding = fundingRepository.findById(fundingPk)
            .orElseThrow(() -> new NotFoundException("해당하는 펀딩이 존재하지 않습니다"));
    Boolean checkDonate = fundingDonateRepository.existsByUserAndFunding(user,funding);

    if(!checkDonate){
      funding.addCurrentAmount();
      FundingDonate fundingDonateList = FundingDonate.builder()
              .funding(funding)
              .user(user)
              .build();
      fundingDonateRepository.save(fundingDonateList);
    }
  }


  // 유저가 좋아요한 펀딩 전체 조회
  @Transactional(readOnly = true)
  public FundingDto.MyFundingLikeResult findAllFundingLikeByUser(Users user){
    List<FundingLike> fundingLikeList = fundingLikeRepository.findByUser(user);
    if(fundingLikeList.isEmpty()){
      throw new NotFoundException("해당 유저가 좋아요한 펀딩이 없습니다");
    }

    List<FundingDto.MyFundingLike> myFundingLikeList = fundingLikeList.stream().map(fundingLike -> new FundingDto.MyFundingLike(
            fundingLike.getPk(),
            fundingLike.getFunding().getTitle(),
            fundingLike.getFunding().getCategory().getFirstCategory().getName(),
            fundingLike.getFunding().getCategory().getName(),
            fundingLike.getUser().getNickname(),
            fundingLike.getFunding().getContent(),
            fundingLike.getFunding().getCurrentAmount(),
            fundingLike.getFunding().getTargetAmount(),
            fundingLike.getFunding().getMainImg(),
            fundingLike.getFunding().getDDay()
    )).toList();

    return new FundingDto.MyFundingLikeResult(myFundingLikeList.size(), myFundingLikeList);

    
  }

  public List<FundingDto.MyFundingResponse> findAllDonateByUser(Users user){  //유저가 후원한 펀딩 전체 출력
    List<FundingDonate> fundingDonateList = fundingDonateRepository.findByUser(user);
    if(fundingDonateList.isEmpty()){
      throw new NotFoundException("해당 유저의 펀딩이 없습니다");
    }
    List<FundingDto.MyFundingResponse> fundingList = new ArrayList<>();
    for(FundingDonate donate : fundingDonateList){
      Funding funding = donate.getFunding();
      List<SecondCategory> secondCategoryList = secondCategoryRepository.findAllByFirstCategory(funding.getCategory().getFirstCategory());
      List<String> secondCategoryNameList = new ArrayList<>();
      for(SecondCategory name : secondCategoryList){
        secondCategoryNameList.add(name.getName());
      }
      FundingDto.MyFundingResponse myFunding = FundingDto.MyFundingResponse.builder()
              .funding_pk(funding.getPk())
              .title(funding.getTitle())
              .first_category_name(funding.getCategory().getFirstCategory().getName())
              .second_category_name(secondCategoryNameList)
              .owner_user_name(funding.getUser().getNickname())
              .description(funding.getContent())
              .current_amount(funding.getCurrentAmount())
              .target_amount(funding.getTargetAmount())
              .main_img(funding.getMainImg())
              .DDay(funding.getDDay())
              .build();
      fundingList.add(myFunding);
    }
    return fundingList;
  }

}
