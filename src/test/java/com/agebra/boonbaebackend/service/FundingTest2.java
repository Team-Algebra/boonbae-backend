package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Funding;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.domain.funding.FirstCategory;
import com.agebra.boonbaebackend.domain.funding.ResearchType;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import com.agebra.boonbaebackend.dto.FundingDto;
import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.exception.ForbiddenException;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.FirstCategoryRepository;
import com.agebra.boonbaebackend.repository.FundingRepository;
import com.agebra.boonbaebackend.repository.SecondCategoryRepository;
import com.agebra.boonbaebackend.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //스프링부트 테스트 시 필요한 의존성 제공
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //클래스 단위로 테스트 진행 - 메소드별 각각 인스턴스 생성되지않음 -> 영향끼침. 순서지정필요
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //
public class FundingTest2 {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private FirstCategoryRepository firstCategoryRepository;
    @Autowired
    private SecondCategoryRepository secondCategoryRepository;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private UserService userService;
    @Autowired
    private FundingRepository fundingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    String title = "title";
    Long target_amount = 10000000L;
    Long supporting_amount = 10000L;
    Long second_category_pk = 1L;
    String second_category_name="2차카테고리";
    Long first_category_pk = 1L;
    String first_category_name="1차카테고리";
    String introduction = "asdf";
    LocalDate open_date = LocalDate.now();
    LocalDate close_date = LocalDate.now();
    String main_image = "asdf";

    @BeforeAll
    void setUp() {
    }
    @DisplayName("펀딩추가")
    @Test
    @Transactional //rollback
    @Order(1)
    void addFunding() {
        FundingDto.AddFunding dto = FundingDto.AddFunding.builder()
                .title(title)
                .target_amount(target_amount)
                .supporting_amount(supporting_amount)
                .second_category_pk(second_category_pk)
                .introduction(introduction)
                .open_date(open_date)
                .close_date(close_date)
                .main_image(main_image)
                .build();

        UserDto.RegisterRequest request = new UserDto.RegisterRequest("asdf1", "asdf1", "asdf1", "");
        Users user = userService.register(request);

        Funding saveFunding = fundingService.addFunding(dto, user);

        assertEquals(title, saveFunding.getTitle());
        assertEquals(target_amount, saveFunding.getTargetAmount());
        assertEquals(supporting_amount, saveFunding.getSupportingAmount());
        assertEquals(second_category_pk, saveFunding.getCategory().getPk());
        assertEquals(introduction, saveFunding.getContent());
        assertEquals(open_date, saveFunding.getOpenDate());
        assertEquals(close_date, saveFunding.getCloseDate());
        assertEquals(main_image, saveFunding.getMainImg());
    }

    @DisplayName("펀딩삭제 - 자기가 올린 펀딩 지우기 시도")
    @Test
    @Transactional //rollback
    @Order(2)
    void deleteFunding() {
        FundingDto.AddFunding dto = FundingDto.AddFunding.builder()
                .title(title)
                .target_amount(target_amount)
                .supporting_amount(supporting_amount)
                .second_category_pk(second_category_pk)
                .introduction(introduction)
                .open_date(open_date)
                .close_date(close_date)
                .main_image(main_image)
                .build();

        UserDto.RegisterRequest request = new UserDto.RegisterRequest("asdf1", "asdf1", "asdf1", "");
        Users user = userService.register(request);

        Funding saveFunding = fundingService.addFunding(dto, user);

        assertThrows(ForbiddenException.class, () -> {
            fundingService.deleteFunding(saveFunding.getPk(), user);
        });
    }

    @DisplayName("펀딩삭제 - 관리자가 지우기 시도")
    @Test
    @Transactional //rollback
    @Order(3)
    void deleteFundingAdmin() {

        FundingDto.AddFunding dto = FundingDto.AddFunding.builder()
                .title(title)
                .target_amount(target_amount)
                .supporting_amount(supporting_amount)
                .second_category_pk(second_category_pk)
                .introduction(introduction)
                .open_date(open_date)
                .close_date(close_date)
                .main_image(main_image)
                .build();

        //관리자아이디
        UserDto.RegisterRequest request = new UserDto.RegisterRequest("asdf781", "asdf781", "asdf781", "");
        Users user = userService.register(request);
        user.setAdmin();

        Funding saveFunding = fundingService.addFunding(dto, user);

        fundingService.deleteFunding(saveFunding.getPk(), user);

        assertThrows(NotFoundException.class, () -> {
            fundingService.one_funding(saveFunding.getPk());
        });
    }

    @DisplayName("관리자 펀딩 개별 승인")
    @Test
    @Transactional //rollback
    @Order(4)
    void approveFundingAdmin() {

        FundingDto.AddFunding dto = FundingDto.AddFunding.builder()
                .title(title)
                .target_amount(target_amount)
                .supporting_amount(supporting_amount)
                .second_category_pk(second_category_pk)
                .introduction(introduction)
                .open_date(open_date)
                .close_date(close_date)
                .main_image(main_image)
                .build();

        //관리자아이디
        UserDto.RegisterRequest request = new UserDto.RegisterRequest("asdf781", "asdf781", "asdf781", "");
        Users user = userService.register(request);
        user.setAdmin();

        Funding saveFunding = fundingService.addFunding(dto, user);

//      fundingService.approve(saveFunding.getPk());
//      해당 코드 실행 안해야 성공적으로 작동됨
        FundingDto.MyFundingResult myFundingResult = fundingService.List_Funding_DeAccess();

        boolean hasData = false;
        for (FundingDto.MyFunding result : myFundingResult.getFundingList()) {
            if (result.getFunding_pk() == saveFunding.getPk()) {
                hasData = true;
                break;
            }
        }

        assertTrue(hasData);
    }

    @DisplayName("펀딩좋아요")
    @Test
    @Transactional //rollback
    @Order(5)
    void likeFunding() {

        FundingDto.AddFunding dto = FundingDto.AddFunding.builder()
                .title(title)
                .target_amount(target_amount)
                .supporting_amount(supporting_amount)
                .second_category_pk(second_category_pk)
                .introduction(introduction)
                .open_date(open_date)
                .close_date(close_date)
                .main_image(main_image)
                .build();

        UserDto.RegisterRequest request = new UserDto.RegisterRequest("asdf1", "asdf1", "asdf1", "");
        Users user = userService.register(request);

        Funding saveFunding = fundingService.addFunding(dto, user);

        fundingService.addLikeToFunding(user, saveFunding.getPk());

        FundingDto.MyFundingResult allFundingLikeByUser = fundingService.findAllFundingLikeByUser(user);

        boolean hasData = false;
        for (FundingDto.MyFunding result : allFundingLikeByUser.getFundingList()) {
            if (result.getFunding_pk() == saveFunding.getPk()) {
                hasData = true;
                break;
            }
        }

        assertTrue(hasData);
    }

    @DisplayName("내가 후원한 펀딩")
    @Test
    @Transactional //rollback
    @Order(6)
    void myFunding() {
        FundingDto.AddFunding dto = FundingDto.AddFunding.builder()
                .title(title)
                .target_amount(target_amount)
                .supporting_amount(supporting_amount)
                .second_category_pk(second_category_pk)
                .introduction(introduction)
                .open_date(open_date)
                .close_date(close_date)
                .main_image(main_image)
                .build();

        //관리자아이디
        UserDto.RegisterRequest request = new UserDto.RegisterRequest("asdf781", "asdf781", "asdf781", "");
        Users user = userService.register(request);
        user.setAdmin();

        Funding saveFunding = fundingService.addFunding(dto, user);

        fundingService.approve(saveFunding.getPk());

        FundingDto.MyFundingResult myFundingResult = fundingService.findAllMakeUser(user);

        boolean hasData = false;
        for (FundingDto.MyFunding result : myFundingResult.getFundingList()) {
            if (result.getFunding_pk() == saveFunding.getPk()) {
                hasData = true;
                break;
            }
        }

        assertTrue(hasData);
    }
    @DisplayName("전체 펀딩 category 확인")
    @Test
    @Order(7)
    @Transactional  //안하면 롤백 x ( db에 해당 값이 저장되버림 -> 중복 오류)
    void allFunding() {
        FirstCategory firstCategory = FirstCategory.builder()
                .pk(first_category_pk)
                .name(first_category_name)
                .build();
        SecondCategory secondCategory = SecondCategory.builder()
                .pk(second_category_pk)
                .name(second_category_name)
                .firstCategory(firstCategory)
                .build();
        FundingDto.AddFunding dto = FundingDto.AddFunding.builder()
                .title(title)
                .target_amount(target_amount)
                .supporting_amount(supporting_amount)
                .second_category_pk(secondCategory.getPk())
                .introduction(introduction)
                .open_date(open_date)
                .close_date(close_date)
                .main_image(main_image)
                .build();
        firstCategoryRepository.save(firstCategory);
        secondCategoryRepository.save(secondCategory);
        //관리자아이디
        UserDto.RegisterRequest request = new UserDto.RegisterRequest("asdf781", "asdf781", "asdf781", "");
        Users user = userService.register(request);
        user.setAdmin();
        Funding saveFunding = fundingService.addFunding(dto, user);
        fundingRepository.save(saveFunding);
        fundingService.approve(saveFunding.getPk());
        Pageable pageable = PageRequest.of(0, 10);
        List<FundingDto.MyFunding> myFundingResult = fundingService.Page_Funding(user, pageable, ResearchType.RECENT, first_category_name,title).getFundingList().getContent();
        boolean hasData = false;
        for (FundingDto.MyFunding result : myFundingResult) {
            if (result.getFunding_pk() == saveFunding.getPk()) {
                hasData = true;
                break;
            }
        }
        assertTrue(hasData);
    }

}
