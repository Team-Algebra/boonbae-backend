package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.*;
import com.agebra.boonbaebackend.domain.funding.FirstCategory;
import com.agebra.boonbaebackend.domain.funding.ResearchType;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import com.agebra.boonbaebackend.dto.FundingDonateDto;
import com.agebra.boonbaebackend.dto.FundingDto;
import com.agebra.boonbaebackend.exception.ForbiddenException;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
  private final FirstCategoryRepository firstCategoryRepository;
  private final FundingLikeRepository fundingLikeRepository;
  private final FundingDonateRepository fundingDonateRepository;
  public Funding addFunding(FundingDto.AddFunding dto, Users user) throws RuntimeException {
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

    Funding save = fundingRepository.save(funding);

    return save;
  }

  public void deleteFunding(Long fundingPk, Users user){
    Funding funding = fundingRepository.findById(fundingPk)
            .orElseThrow(() -> new NotFoundException("해당하는 펀딩이 존재하지 않음"));
    Users fundingUser = funding.getUser();

    //권한없는 user 처리
    if (user.getRole() != UserRole.ADMIN && user.getPk() != funding.getPk()) {
      throw new ForbiddenException("권한이 없는 사용자입니다");
    }
    fundingRepository.delete(funding);
  }

  @Transactional(readOnly = true)  // 페이지로 펀딩 전체출력(카테고리검색)
  public FundingDto.MyFundingResult_Page Page_Funding(Users user,Pageable pageable, ResearchType type, String firstCategoryName,String title) {
    List<Funding> fundingListByType;
    List<Funding> fundingListByCategory;
    List<Funding> fundingListByTitle;
    List<Funding> fundingList_fir;
    List<Funding> fundingList_sec;

    if (type != null) {
      switch(type) { //타입에 따라 나눔
        case IMMINENT:
          fundingListByType = fundingRepository.findByIsAndApprovedTrueOrderByCloseDateAsc();
          break;
        case POPULARITY:
          fundingListByType = fundingRepository.findByIsApprovedTrueOrderByCurrentAmountDivSupportingAmount();
          break;
        default: //최신순 findByIsApprovedTrueOrderByOpenDateDesc
          fundingListByType = fundingRepository.findByIsApprovedTrueOrderByOpenDateDesc();
      }
    } else { //type이 없을 때 최신순
      fundingListByType = fundingRepository.findByIsApprovedTrueOrderByOpenDateDesc();
    }
    if(firstCategoryName!=null){
      FirstCategory firstCategory = firstCategoryRepository.findByName(firstCategoryName)
              .orElseThrow(()-> new NotFoundException("해당하는 단어의 카테고리가 없습니다"));
      fundingListByCategory=fundingRepository.findByCategory_FirstCategory(firstCategory);
      fundingList_fir=new ArrayList<>(fundingListByType);
      fundingList_fir.retainAll(fundingListByCategory);
    }else{
      fundingList_fir=fundingListByType;
    }

    if(title!=null){
      fundingListByTitle=fundingRepository.findByTitleContaining(title);
      fundingList_sec=new ArrayList<>(fundingList_fir);
      fundingList_sec.retainAll(fundingListByTitle);
    }else{
      fundingList_sec=fundingList_fir;
    }
    List<Funding> fundingList = fundingList_sec;
    List<FundingDto.MyFunding> allFundingList = fundingList.stream().map((funding) -> {
      boolean isLike = false;
      if (user != null)
        isLike = fundingLikeRepository.existsByUserAndFunding(user , funding);

      return FundingDto.MyFunding.builder()
              .funding_pk(funding.getPk())
              .title(funding.getTitle())
              .first_category_name(funding.getCategory().getFirstCategory().getName())
              .second_category_name(funding.getCategory().getName())
              .owner_user_name(funding.getUser().getNickname())
              .description(funding.getContent())
              .current_amount(funding.getCurrentAmount())
              .target_amount(funding.getTargetAmount())
              .main_img(funding.getMainImg())
              .DDay(funding.getDDay())
              .isLike(isLike)
              .build();
    }).toList();

    int start = (int) pageable.getOffset();
    int end=Math.min((start + pageable.getPageSize()),allFundingList.size());
    Page<FundingDto.MyFunding> fundingAll= new PageImpl<>(allFundingList.subList(start,end),pageable,allFundingList.size());
    int pageCount = (int)Math.ceil((double)allFundingList.size()/pageable.getPageSize());
    FundingDto.MyFundingResult_Page dto = new FundingDto.MyFundingResult_Page(pageCount,fundingAll);
    return dto;
  }



  @Transactional(readOnly = true) // 리스트로 펀딩 전체 출력( 카테고리 검색)
  public FundingDto.MyFundingResult List_Funding(Users user, ResearchType type, String firstCategoryName,String title) {
    List<Funding> fundingListByType;
    List<Funding> fundingListByCategory;
    List<Funding> fundingListByTitle;
    List<Funding> fundingList_fir;
    List<Funding> fundingList_sec;

    if (type != null) {
      switch(type) { //타입에 따라 나눔
        case IMMINENT:
          fundingListByType = fundingRepository.findByIsAndApprovedTrueOrderByCloseDateAsc();
          break;
        case POPULARITY:
          fundingListByType = fundingRepository.findByIsApprovedTrueOrderByCurrentAmountDivSupportingAmount();
          break;
        default: //최신순 findByIsApprovedTrueOrderByOpenDateDesc
          fundingListByType = fundingRepository.findByIsApprovedTrueOrderByOpenDateDesc();
      }
    } else { //type이 없을 때 최신순
      fundingListByType = fundingRepository.findByIsApprovedTrueOrderByOpenDateDesc();
    }
    if(firstCategoryName!=null){
      FirstCategory firstCategory = firstCategoryRepository.findByName(firstCategoryName)
              .orElseThrow(()-> new NotFoundException("해당하는 단어의 카테고리가 없습니다"));
      fundingListByCategory=fundingRepository.findByCategory_FirstCategory(firstCategory);
      fundingList_fir=new ArrayList<>(fundingListByType);
      fundingList_fir.retainAll(fundingListByCategory);
    }else{
      fundingList_fir=fundingListByType;
    }

    if(title!=null){
      fundingListByTitle=fundingRepository.findByTitleContaining(title);
      fundingList_sec=new ArrayList<>(fundingList_fir);
      fundingList_sec.retainAll(fundingListByTitle);
    }else{
      fundingList_sec=fundingList_fir;
    }
    List<Funding> fundingList = fundingList_sec;
    List<FundingDto.MyFunding> allFundingList = fundingList.stream().map((funding) -> {
      boolean isLike = false;
      if (user != null)
        isLike = fundingLikeRepository.existsByUserAndFunding(user , funding);

      return FundingDto.MyFunding.builder()
          .funding_pk(funding.getPk())
          .title(funding.getTitle())
          .first_category_name(funding.getCategory().getFirstCategory().getName())
          .second_category_name(funding.getCategory().getName())
          .owner_user_name(funding.getUser().getNickname())
          .description(funding.getContent())
          .current_amount(funding.getCurrentAmount())
          .target_amount(funding.getTargetAmount())
          .main_img(funding.getMainImg())
          .DDay(funding.getDDay())
          .isLike(isLike)
          .build();
    }).toList();

//    List<FundingDto.MyFunding> allFundingList = fundingList.stream().map(funding -> new FundingDto.MyFunding(
//            funding.getPk(),
//            funding.getTitle(),
//            funding.getCategory().getFirstCategory().getName(),
//            funding.getCategory().getName(),
//            funding.getUser().getNickname(),
//            funding.getContent(),
//            funding.getCurrentAmount(),
//            funding.getTargetAmount(),
//            funding.getMainImg(),
//            funding.getDDay()
//    )).toList();
    FundingDto.MyFundingResult dto = new FundingDto.MyFundingResult(allFundingList.size(),allFundingList);
    return dto;
  }

  @Transactional(readOnly = true)
  public FundingDto.MyFunding one_funding(Long fundingPk){
    Funding funding = fundingRepository.findById(fundingPk)
            .orElseThrow(() -> new NotFoundException("펀딩을 찾을 수 없습니다"));
    FundingDto.MyFunding fundingDto = FundingDto.MyFunding.builder()
            .funding_pk(funding.getPk())
            .title(funding.getTitle())
            .open_date(funding.getOpenDate())
            .close_date(funding.getCloseDate())
            .supporting_amount(funding.getSupportingAmount())
            .first_category_name(funding.getCategory().getFirstCategory().getName())
            .second_category_name(funding.getCategory().getName())
            .owner_user_name(funding.getUser().getNickname())
            .description(funding.getContent())
            .current_amount(funding.getCurrentAmount())
            .target_amount(funding.getTargetAmount())
            .main_img(funding.getMainImg())
            .DDay(funding.getDDay())
            .build();
    return fundingDto;
  }


  public void fundingAccess(){  // 관리자용 -> 승인안된 funding 일괄수락
    List<Funding> funding = fundingRepository.findByApproved(false);
    
    //stream은 왜 안되지???? 나중에 확인해보기

    for (Funding fund: funding) {
      fund.accessFunding();
    }
  }

  @Transactional(readOnly = true) // 괸리자용 -> 승인안된 funding 확인
  public FundingDto.MyFundingResult List_Funding_DeAccess() {
    List<Funding> fundingList;
    fundingList = fundingRepository.findByApproved(false);

    List<FundingDto.MyFunding> allFundingList = fundingList.stream().map(funding -> FundingDto.MyFunding.builder()
              .funding_pk(funding.getPk())
              .title(funding.getTitle())
              .first_category_name(funding.getCategory().getFirstCategory().getName())
              .second_category_name(funding.getCategory().getName())
              .owner_user_name(funding.getUser().getNickname())
              .description(funding.getContent())
              .current_amount(funding.getCurrentAmount())
              .target_amount(funding.getTargetAmount())
              .main_img(funding.getMainImg())
              .DDay(funding.getDDay())
              .build()
    ).toList();

    FundingDto.MyFundingResult dto = new FundingDto.MyFundingResult(allFundingList.size(),allFundingList);
    return dto;
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

  public boolean paymentCheck(FundingDonateDto.Request_All requestDto, PaymentMethod paymentMethod) { //결제 확인(미완)
    return true;
  }


  @Transactional
  public void addDonateToFunding(Users user, Long fundingPk) {   //펀딩 후원
    Funding funding = fundingRepository.findById(fundingPk)
            .orElseThrow(() -> new NotFoundException("해당하는 펀딩이 존재하지 않습니다"));

    //여러 번 후원가능
//    Boolean checkDonate = fundingDonateRepository.existsByUserAndFunding(user, funding);

    funding.addCurrentAmount();
    FundingDonate fundingDonateList = FundingDonate.builder()
            .funding(funding)
            .user(user)
            .build();

    fundingDonateRepository.save(fundingDonateList);
  }


  // 유저가 좋아요한 펀딩 전체 조회
  @Transactional(readOnly = true)
  public FundingDto.MyFundingResult findAllFundingLikeByUser(Users user) {
    List<FundingLike> fundingLikeList = fundingLikeRepository.findByUser(user);
//    if (fundingLikeList.isEmpty()) {
//      throw new NotFoundException("해당 유저가 좋아요한 펀딩이 없습니다");
//    }

    List<FundingDto.MyFunding> myFundingLikeList = fundingLikeList.stream().map((fundingLike) -> {
      Funding funding = fundingLike.getFunding();

      return FundingDto.MyFunding.builder()
              .funding_pk(funding.getPk())
              .title(funding.getTitle())
              .first_category_name(funding.getCategory().getFirstCategory().getName())
              .second_category_name(funding.getCategory().getName())
              .owner_user_name(funding.getUser().getNickname())
              .description(funding.getContent())
              .current_amount(funding.getCurrentAmount())
              .target_amount(funding.getTargetAmount())
              .main_img(funding.getMainImg())
              .DDay(funding.getDDay())
              .build();
    }).toList();

    return new FundingDto.MyFundingResult(myFundingLikeList.size(), myFundingLikeList);

  }

  @Transactional(readOnly = true)
  public FundingDto.MyFundingResult findAllDonateByUser(Users user) {  //유저가 후원한 펀딩 전체 출력
    List<FundingDonate> fundingDonateList = fundingDonateRepository.findByUser(user);
//    if (fundingDonateList.isEmpty()) {
//      throw new NotFoundException("해당 유저가 후원한 펀딩이 없습니다");
//    }

    List<FundingDto.MyFunding> myFundingDonateList = fundingDonateList.stream().map((fundingDonate) -> {
      Funding funding = fundingDonate.getFunding();

      return FundingDto.MyFunding.builder()
              .funding_pk(funding.getPk())
              .title(funding.getTitle())
              .first_category_name(funding.getCategory().getFirstCategory().getName())
              .second_category_name(funding.getCategory().getName())
              .owner_user_name(funding.getUser().getNickname())
              .description(funding.getContent())
              .current_amount(funding.getCurrentAmount())
              .target_amount(funding.getTargetAmount())
              .main_img(funding.getMainImg())
              .DDay(funding.getDDay())
              .build();
    }).toList();

    return new FundingDto.MyFundingResult(myFundingDonateList.size(), myFundingDonateList);
  }

  @Transactional(readOnly = true)
  public FundingDto.MyFundingResult findAllMakeUser(Users user) {  //유저가 등록한 펀딩 전체 출력
    List<Funding> fundingMakeList = fundingRepository.findByUser(user);
//    if (fundingMakeList.isEmpty()) {
//      throw new NotFoundException("해당 유저가 등록한 펀딩이 없습니다");
//    }

    List<FundingDto.MyFunding> myFundingMakeList = fundingMakeList.stream().map(funding -> FundingDto.MyFunding.builder()
            .funding_pk(funding.getPk())
            .title(funding.getTitle())
            .first_category_name(funding.getCategory().getFirstCategory().getName())
            .second_category_name(funding.getCategory().getName())
            .owner_user_name(funding.getUser().getNickname())
            .description(funding.getContent())
            .current_amount(funding.getCurrentAmount())
            .target_amount(funding.getTargetAmount())
            .main_img(funding.getMainImg())
            .DDay(funding.getDDay())
            .build()
    ).toList();

    return new FundingDto.MyFundingResult(myFundingMakeList.size(), myFundingMakeList);
  }
  // 나의 진행중인 펀딩 조회(최신순)
  @Transactional(readOnly = true)
  public FundingDto.MyFundingResult findOngoingFundingByUser(Users user){
    List<Funding> ongoingFundingList = fundingRepository.findOngoingFundingByUser(user);
//    if(ongoingFundingList.isEmpty()){
//      throw new NotFoundException("해당 유저가 진행중인 펀딩이 없습니다");
//    }

    List<FundingDto.MyFunding> myFundingList = ongoingFundingList.stream().map(funding -> FundingDto.MyFunding.builder()
            .funding_pk(funding.getPk())
            .title(funding.getTitle())
            .first_category_name(funding.getCategory().getFirstCategory().getName())
            .second_category_name(funding.getCategory().getName())
            .owner_user_name(funding.getUser().getNickname())
            .description(funding.getContent())
            .current_amount(funding.getCurrentAmount())
            .target_amount(funding.getTargetAmount())
            .main_img(funding.getMainImg())
            .DDay(funding.getDDay())
            .build()
    ).toList();

    return new FundingDto.MyFundingResult(myFundingList.size(), myFundingList);
  }

  @Transactional(readOnly = true)
  public void approve(Long fundingPk) {
    Funding funding = fundingRepository.findById(fundingPk)
            .orElseThrow(() -> new NotFoundException("해당하는 펀딩을 찾을 수 없습니다"));

    //펀딩승인
    funding.accessFunding();
  }

}



