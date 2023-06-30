package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.domain.TrashType;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.repository.RecyclingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RecyclingServiceTest {
    @Mock
    private RecyclingRepository recyclingRepository;

    @InjectMocks
    private RecyclingService recyclingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("분리배출 정보 검색 테스트")
    void searchRecyclingInfo_Test() {
        // 검색어와 일치하는 정보를 생성
        RecyclingInfo info1 = RecyclingInfo.makeRecyclingInfo("Paper Recycling", TrashType.PAPER, "Process 1", "Description 1", "image1.jpg");
        RecyclingInfo info2 = RecyclingInfo.makeRecyclingInfo("Plastic Recycling", TrashType.PLASTIC, "Process 2", "Description 2", "image2.jpg");
        List<RecyclingInfo> mockInfoList = new ArrayList<>();
        mockInfoList.add(info1);
        mockInfoList.add(info2);

        // Mock repository의 동작 설정
        when(recyclingRepository.findByKeyword("Recycling")).thenReturn(mockInfoList);

        // 검색어로 검색된 결과 확인
        RecyclingDto.SearchResult result = recyclingService.searchRecyclingInfo("Recycling");

        // 기대값과 실제값 비교
        assertEquals(2, result.getCount());
        assertEquals(mockInfoList, result.getInfoList());
    }
}
