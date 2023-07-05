package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Funding;
import com.agebra.boonbaebackend.domain.UserRole;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.domain.funding.FirstCategory;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import com.agebra.boonbaebackend.repository.FundingRepository;
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
class FundingTest {
    @Mock
    private FundingRepository fundingRepository;

    @InjectMocks
    private FundingService fundingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    @DisplayName("관리자 승인 확인")
    void AdminFunding_Test() {
        Users user1 = Users.builder()
                .id("id1")
                .password("pass1")
                .nickname("nick1")
                .role(UserRole.ADMIN)
                .build();
        FirstCategory firstCategory1 = FirstCategory.builder()
                .name("카테고리1")
                .build();
        SecondCategory secondCategory1 = SecondCategory.builder()
                .firstCategory(firstCategory1)
                .name("카테고리2")
                .build();
        Funding funding1 = Funding.builder()
                .pk(1L)
                .category(secondCategory1)
                .closeDate(LocalDate.now())
                .title("테스트펀딩1")
                .content("테스트펀딩1 설명")
                .mainImg("https~")
                .targetAmount(10000L)
                .supportingAmount(1000L)
                .build();
        Funding funding2 = Funding.builder()
                .pk(2L)
                .category(secondCategory1)
                .closeDate(LocalDate.now())
                .title("테스트펀딩2")
                .content("테스트펀딩2 설명")
                .mainImg("https~")
                .targetAmount(10000L)
                .supportingAmount(1000L)
                .isApproved(true)
                .build();
        Funding funding3 = Funding.builder()
                .pk(3L)
                .category(secondCategory1)
                .closeDate(LocalDate.now())
                .title("테스트펀딩2")
                .content("테스트펀딩2 설명")
                .mainImg("https~")
                .targetAmount(10000L)
                .supportingAmount(1000L)
                .build();
        fundingRepository.save(funding1);
        fundingRepository.save(funding2);
        when(fundingRepository.findByApproved(false)).thenReturn(List.of(funding1,funding3));
        List<Funding> fundingDeAccessList = fundingRepository.findByApproved(false);
        List<Funding> fundingList = new ArrayList<>();
        fundingList.add(funding1);
        fundingList.add(funding3);
        assertEquals(fundingDeAccessList,fundingList);

        when(fundingRepository.findByApproved(true)).thenReturn(List.of(funding2));
        List<Funding> fundingAccessList = fundingRepository.findByApproved(true);
        List<Funding> fundingList2 = new ArrayList<>();
        fundingList2.add(funding2);

        assertEquals(fundingAccessList,fundingList2);
    }
}