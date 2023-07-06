package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.domain.PaymentMethod;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.CategoryDto;
import com.agebra.boonbaebackend.dto.FundingDonateDto;
import com.agebra.boonbaebackend.dto.FundingDto;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.service.CategoryService;
import com.agebra.boonbaebackend.service.FundingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "FundingController", description = "Funding 관련한 컨트롤러 입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/funding")
public class FundingController {
  private final FundingService fundingService;
  private final CategoryService categoryService;

  @PostMapping("/") //펀딩 추가
  public ResponseEntity addFunding(@RequestBody FundingDto.AddFunding dto, @AuthenticationPrincipal Users user) {
    fundingService.addFunding(dto, user);

    return ResponseEntity.ok().build();
  }
  @GetMapping("/")  //펀딩 전체 리스트 (관리자가 승인 안한것은 출력 x)
  public ResponseEntity<FundingDto.MyFundingResult> findAllFunding(){
    FundingDto.MyFundingResult dto = fundingService.List_Funding();
    return ResponseEntity.ok().body(dto);
  }
  @GetMapping("/{funding_pk}") //펀딩 상세정보 페이지
  public ResponseEntity<FundingDto.MyFunding> findOneFunding(@PathVariable("funding_pk") Long fundingPk){
    FundingDto.MyFunding dto = fundingService.one_funding(fundingPk);
    return ResponseEntity.ok().body(dto);
  }

  @Secured("ADMIN")
  @DeleteMapping("/{funding_pk}") //펀딩 삭제
  public ResponseEntity<Void> deleteFunding(@AuthenticationPrincipal Users user ,@PathVariable("funding_pk") Long fundingPk){
    fundingService.deleteFunding(fundingPk,user);
    return ResponseEntity.ok().build();
  }

  @Secured("ADMIN")
  @PostMapping("/category/first") //첫 번째 카테고리 추가
  public ResponseEntity addFirstCategory(@RequestBody Map<String, String> map) {
    String name = map.get("name");
    if (name == null)
      throw new InputMismatchException("addFirstCategory: name은 필수입니다");
    categoryService.addFirstCategory(name);

    return ResponseEntity.ok().build();
  }

  @Secured("ADMIN")
  @PostMapping("/category/second") //두 번째 카테고리 추가
  public ResponseEntity addSecondCategory(@RequestBody @Valid CategoryDto.Add dto) {
    categoryService.addSecondCategory(dto);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/category") //전체조회
  public ResponseEntity getCategoryList() {
    CategoryDto.All all = categoryService.getAll();

    return ResponseEntity.ok(all);
  }

  // 펀딩 좋아요 추가 (Only User)
  @PostMapping("/{funding_pk}/likes")
  public ResponseEntity addLikeToFunding(@AuthenticationPrincipal Users user, @PathVariable("funding_pk") Long fundingPk) {
    fundingService.addLikeToFunding(user, fundingPk);
    return ResponseEntity.ok().build();
  }

  // 펀딩 좋아요 제거 (Only User)
  @DeleteMapping("/{funding_pk}/likes")
  public ResponseEntity removeLikeFromComment(@AuthenticationPrincipal Users user, @PathVariable("funding_pk") Long fundingPk) {
    fundingService.removeLikeFromFunding(user, fundingPk);
    return ResponseEntity.ok().build();
  }

  // 내가 좋아요한 펀딩 조회
  @GetMapping("/like")
  public ResponseEntity<FundingDto.MyFundingResult> findAllFundingLikeByUser(@AuthenticationPrincipal Users user){
    FundingDto.MyFundingResult myFundingLikeResult = fundingService.findAllFundingLikeByUser(user);
    return ResponseEntity.ok(myFundingLikeResult);
  }

  @PostMapping("/{funding_pk}/sponsor") //펀딩 후원(결제기능 미구현)
  public ResponseEntity addFundingDoanate(@RequestParam(value = "PaymentMethod",required = true)PaymentMethod paymentMethod,
                                          @AuthenticationPrincipal Users user,
                                          @PathVariable("funding_pk") Long fundingPk,
                                          @RequestBody FundingDonateDto.Request_All dto){
    if(dto==null){
      throw new NotFoundException("올바르지 않은 값이 입력되었습니다");
    }
    Boolean checkPayment = fundingService.paymentCheck(dto,paymentMethod);
    if(!checkPayment){
      throw new NotFoundException("결제에 실패하였습니다 잘못된 정보가 없는지 확인해주세요");
    }
    fundingService.addDonateToFunding(user, fundingPk);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/donate") //사용자 후원 리스트 확인
  public ResponseEntity<FundingDto.MyFundingResult> findAllFundingDonateByUser(@AuthenticationPrincipal Users user){
     FundingDto.MyFundingResult fundingDonateList = fundingService.findAllDonateByUser(user);
     return ResponseEntity.ok().body(fundingDonateList);
  }

  @GetMapping("/my") // 사용자 만든 펀딩 리스트 확인
  public ResponseEntity<FundingDto.MyFundingResult> findAllFundingMakeByUser(@AuthenticationPrincipal Users user){
    FundingDto.MyFundingResult fundingMakeList = fundingService.findAllMakeUser(user);
    return ResponseEntity.ok().body(fundingMakeList);
  }

  // 나의 진행중인 펀딩 조회(최신순)
  @GetMapping("/my/ongoing")
  public ResponseEntity<FundingDto.MyFundingResult> MyOngoingFundingList(@AuthenticationPrincipal Users user){
    FundingDto.MyFundingResult userFundingList = fundingService.findOngoingFundingByUser(user);
    return ResponseEntity.ok().body(userFundingList);
  }

  @Secured("ADMIN")
  @PostMapping("/access") //승인안된 funding 리스트 전체승인 (관리자 전용)
  public ResponseEntity<Void> AccessFunding(@RequestBody List<Long> fundingPk){
    fundingService.fundingAccess(fundingPk);
    return ResponseEntity.ok().build();
  }

  @Secured("ADMIN")
  @GetMapping("/access") // 승인안된 funding 리스트 전체출력(관리자 전용)
  public ResponseEntity<FundingDto.MyFundingResult> findAllDeAccess(){
    FundingDto.MyFundingResult dto = fundingService.List_Funding_DeAccess();
    return ResponseEntity.ok().body(dto);
  }

  //관리자가 펀딩 승인
  @Secured("ADMIN")
  @GetMapping("/{funding_pk}/approve") // 승인안된 funding 리스트 전체출력(관리자 전용)
  public ResponseEntity<FundingDto.MyFundingResult> approveFunding(@RequestParam(value = "funding_pk",required = true) Long fundingPk){
    fundingService.approve(fundingPk);


    return ResponseEntity.ok().build();
  }

}
