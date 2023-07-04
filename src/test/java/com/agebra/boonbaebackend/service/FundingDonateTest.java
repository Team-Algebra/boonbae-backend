package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Funding;
import com.agebra.boonbaebackend.domain.FundingDonate;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.domain.funding.FirstCategory;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import com.agebra.boonbaebackend.repository.FundingDonateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest
class FundingDonateTest{
    @Mock
    private FundingDonateRepository fundingDonateRepository;

    @InjectMocks
    private FundingService fundingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("유저 후원 펀딩 확인")
    void UserFundingDonate_Test() {

        Users user1 = Users.builder()
                .id("id1")
                .password("pass1")
                .nickname("nick1")
                .build();
        FirstCategory firstCategory1 = FirstCategory.builder()
                .name("카테고리1")
                .build();
        SecondCategory secondCategory1 = SecondCategory.builder()
                .firstCategory(firstCategory1)
                .name("카테고리2")
                .build();
        Funding funding1 = Funding.builder()
                .category(secondCategory1)
                .closeDate(LocalDate.now())
                .title("테스트펀딩1")
                .content("테스트펀딩1 설명")
                .mainImg("https~")
                .targetAmount(10000L)
                .supportingAmount(1000L)
                .build();
        Funding funding2 = Funding.builder()
                .category(secondCategory1)
                .closeDate(LocalDate.now())
                .title("테스트펀딩2")
                .content("테스트펀딩2 설명")
                .mainImg("https~")
                .targetAmount(10000L)
                .supportingAmount(1000L)
                .build();
        FundingDonate fundingDonate1 = FundingDonate.builder()
                .funding(funding1)
                .user(user1)
                .build();
        FundingDonate fundingDonate2 = FundingDonate.builder()
                .funding(funding2)
                .user(user1)
                .build();
        fundingDonateRepository.save(fundingDonate1);
        fundingDonateRepository.save(fundingDonate2);

        when(fundingDonateRepository.findByUser(user1)).thenReturn(List.of(fundingDonate1,fundingDonate2));
        List<FundingDonate> fundingDonateList = fundingDonateRepository.findByUser(user1);
        List<FundingDonate> fundingDonates = new ArrayList<>();
        fundingDonates.add(fundingDonate1);
        fundingDonates.add(fundingDonate2);
        assertEquals(fundingDonates,fundingDonateList);
    }
}

