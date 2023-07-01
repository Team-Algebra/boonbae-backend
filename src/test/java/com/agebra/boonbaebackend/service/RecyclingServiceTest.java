package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.domain.TrashType;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.repository.RecyclingRepository;
import com.agebra.boonbaebackend.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecyclingServiceTest {
    @MockBean
    private RecyclingRepository recyclingRepository;

    @MockBean
    private TagRepository tagRepository;


    @InjectMocks
    private RecyclingService recyclingService;

    @BeforeEach
    void setup() {
        recyclingRepository = Mockito.mock(RecyclingRepository.class);
        tagRepository = Mockito.mock(TagRepository.class);
        MockitoAnnotations.openMocks(this);

    }



//    @Test
//    @DisplayName("분리배출 정보 검색 테스트")
//    void searchRecyclingInfo_Test() {
//        // 검색어와 일치하는 정보를 생성
//        RecyclingInfo info1 = RecyclingInfo.makeRecyclingInfo("Paper Recycling", TrashType.PAPER, "Process 1", "Description 1", "image1.jpg");
//        RecyclingInfo info2 = RecyclingInfo.makeRecyclingInfo("Plastic Recycling", TrashType.PLASTIC, "Process 2", "Description 2", "image2.jpg");
//        List<RecyclingInfo> mockInfoList = new ArrayList<>();
//        mockInfoList.add(info1);
//        mockInfoList.add(info2);
//
//        // Mock repository의 동작 설정
//        when(recyclingRepository.findByKeyword("Recycling")).thenReturn(mockInfoList);
//
//        // 검색어로 검색된 결과 확인
//        RecyclingDto.SearchResult result = recyclingService.searchRecyclingInfo("Recycling");
//
//        // 기대값과 실제값 비교
//        assertEquals(2, result.getCount());
//        assertEquals(mockInfoList, result.getInfoList());
//    }


    @Test
    @DisplayName("분리배출 정보 검색 테스트")
    void searchRecyclingInfo_Test() {
        // Given
        RecyclingService recyclingService = new RecyclingService(recyclingRepository, tagRepository);
        String keyword = "Recycling";
        List<RecyclingInfo> mockInfoList = new ArrayList<>();

        RecyclingInfo info1 = RecyclingInfo.makeRecyclingInfo("Paper Recycling", TrashType.PAPER, "Process 1", "Description 1", "image1.jpg");
        RecyclingInfo info2 = RecyclingInfo.makeRecyclingInfo("Plastic Recycling", TrashType.PLASTIC, "Process 2", "Description 2", "image2.jpg");
        RecyclingInfo info3 = RecyclingInfo.makeRecyclingInfo("Glass Recycling", TrashType.GLASS, "Process 3", "Description 3", "image3.jpg");

        mockInfoList.add(info1);
        mockInfoList.add(info2);
        mockInfoList.add(info3);

        when(recyclingRepository.findByKeyword(keyword)).thenReturn(mockInfoList);

        // When
        RecyclingDto.SearchResult result = recyclingService.searchRecyclingInfo(keyword);

        // Then
        assertEquals(3, result.getCount()); // 개수가 3인 것을 기대합니다
        assertEquals(mockInfoList, result.getInfoList());
    }

    @Test
    @DisplayName("특정 분리배출 정보 가져오기 테스트")

    void getRecyclingInfoDetail_Test() {
//        // Given
//        Long recyclePk = 1L;
//        RecyclingInfo expectedInfo = RecyclingInfo.builder().pk(recyclePk).build();
//
//        // Mocking the behavior of recyclingRepository
//        when(recyclingRepository.findById(recyclePk)).thenReturn(Optional.of(expectedInfo));
//
//        // When
//        RecyclingDto.DetailResult resultInfo = recyclingService.getRecyclingInfoDetail(recyclePk);
//
//        // Then
//        assertEquals(expectedInfo, resultInfo);
//
//        // Verify that updateViewCount method is called once with the specified recyclePk
//        verify(recyclingRepository, Mockito.times(1)).updateViewCount(recyclePk);
    }





}
