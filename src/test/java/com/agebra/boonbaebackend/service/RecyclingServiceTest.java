package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.domain.Tag;
import com.agebra.boonbaebackend.domain.TrashType;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.RecyclingRepository;
import com.agebra.boonbaebackend.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecyclingServiceTest {

    @Mock
    private RecyclingRepository recyclingRepository;

    @Mock
    private TagRepository tagRepository;

    private RecyclingService recyclingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        recyclingService = new RecyclingService(recyclingRepository, tagRepository);
    }


    @Test
    @DisplayName("분리배출 정보 등록 테스트")
    void write_Test() {
        // given
        RecyclingDto.Write writeDto = new RecyclingDto.Write(
                "Paper",
                "General",
                "Recycle",
                TrashType.PAPER,
                "paper_image.jpg",
                new String[]{"recycle", "paper"}
        );

        Tag existingTag = Tag.makeTag(null, "recycle");

        when(tagRepository.findByName("recycle")).thenReturn(existingTag);
        when(recyclingRepository.save(any())).thenReturn(null);
        when(tagRepository.save(any())).thenReturn(null);

        // when
        assertDoesNotThrow(() -> recyclingService.write(writeDto));

        // then
        verify(recyclingRepository, times(1)).save(any(RecyclingInfo.class));
        verify(tagRepository, times(1)).save(any(Tag.class));
    }


    @Test
    @DisplayName("분리배출 정보 검색 테스트")
    void searchResultDto_Test() {
        // given
        int count = 2;
        List<RecyclingDto.Search> infoList = Arrays.asList(
                new RecyclingDto.Search(1L, "Paper", "Recycle", "Recycle paper waste", TrashType.PAPER,
                        "paper_image.jpg", new String[]{"recycle", "paper"}, 0, LocalDate.now()),
                new RecyclingDto.Search(2L, "Plastic", "Recycle", "Recycle plastic waste", TrashType.PLASTIC,
                        "plastic_image.jpg", new String[]{"recycle", "plastic"}, 0, LocalDate.now())
        );

        // when
        RecyclingDto.SearchResult searchResultDto = new RecyclingDto.SearchResult(count, infoList);

        // then
        assertEquals(count, searchResultDto.getCount());
        assertEquals(infoList, searchResultDto.getInfoList());
    }


    @Test
    @DisplayName("RecyclingService.getRecyclingInfoDetail 테스트")
    void getRecyclingInfoDetail_Test() {
        // given
        Long recyclePk = 1L;
        RecyclingInfo recyclingInfo = RecyclingInfo.builder()
                .pk(recyclePk)
                .name("Paper")
                .process("Recycle")
                .description("Recycle paper waste")
                .type(TrashType.PAPER)
                .imageUrl("paper_image.jpg")
                .tagList(Arrays.asList(Tag.builder().name("recycle").build(), Tag.builder().name("paper").build()))
                .viewCnt(0)
                .createDate(LocalDate.now())
                .build();

        when(recyclingRepository.findById(recyclePk)).thenReturn(Optional.of(recyclingInfo));
        when(recyclingRepository.save(recyclingInfo)).thenReturn(recyclingInfo);

        // when
        RecyclingDto.DetailResult detailResult = recyclingService.getRecyclingInfoDetail(recyclePk);

        // then
        assertEquals("Paper", detailResult.getName());
        assertEquals("Recycle", detailResult.getProcess());
        assertEquals("Recycle paper waste", detailResult.getDescription());
        assertEquals(TrashType.PAPER, detailResult.getType());
        assertEquals("paper_image.jpg", detailResult.getImageUrl());
        assertArrayEquals(new String[]{"recycle", "paper"}, detailResult.getTags());
        assertEquals(1, detailResult.getViewCnt());
        assertNotNull(detailResult.getCreateDate());

        verify(recyclingRepository, times(1)).save(recyclingInfo);
    }


    @Test
    @DisplayName("분리배출 정보 검색 테스트2")
    void searchDto_Test() {
        Long pk = 1L;
        String name = "Paper";
        String process = "Recycle";
        String description = "Recycle paper waste";
        TrashType type = TrashType.PAPER;
        String imageUrl = "paper_image.jpg";
        String[] tags = {"recycle", "paper"};
        int viewCnt = 0;
        LocalDate createDate = LocalDate.now();

        // when
        RecyclingDto.Search searchDto = new RecyclingDto.Search(pk, name, process, description, type,
                imageUrl, tags, viewCnt, createDate);

        // then
        assertEquals(pk, searchDto.getPk());
        assertEquals(name, searchDto.getName());
        assertEquals(process, searchDto.getProcess());
        assertEquals(description, searchDto.getDescription());
        assertEquals(type, searchDto.getType());
        assertEquals(imageUrl, searchDto.getImageUrl());
        assertEquals(tags, searchDto.getTags());
        assertEquals(viewCnt, searchDto.getViewCnt());
        assertEquals(createDate, searchDto.getCreateDate());
    }


    @Test
    @DisplayName("분리배출 정보 검색 테스트 - 분리배출 정보가 없을 때")
    void testSearchRecyclingInfo_NotFound() {
        String keyword = "이름";

        when(recyclingRepository.findByKeyword(keyword)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> recyclingService.searchRecyclingInfo(keyword));

        verify(recyclingRepository, times(1)).findByKeyword(keyword);
    }

}
