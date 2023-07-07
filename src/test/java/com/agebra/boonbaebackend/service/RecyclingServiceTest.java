package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.dto.RecyclingDto;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.RecyclingInfoTagRepository;
import com.agebra.boonbaebackend.repository.RecyclingInfoTypeRepository;
import com.agebra.boonbaebackend.repository.RecyclingRepository;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecyclingServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RecyclingService recyclingService;

    @Autowired
    private RecyclingRepository recyclingRepository;
    @Autowired
    private RecyclingInfoTagRepository recyclingInfoTagRepository;
    @Autowired
    private RecyclingInfoTypeRepository recyclingInfoTypeRepository;


    private String name;
    private String[] process;
    private String[] description;
    private String[] types;
    private String image_url;
    private String[] tags;


    @BeforeEach
    void setup() {
        name = "종이";
        process = new String[]{"과정"};
        description =  new String[]{"설명"};
        types = new String[]{"종이"};
        image_url = "paper_image.jpg";
        tags = new String[]{"paper", "recycle"};

    }

    @Test
    @DisplayName("분리배출 정보 등록 테스트")
    @Transactional //rollback
    void write() {
        RecyclingDto.Write writeDto = new RecyclingDto.Write(
                name, process, description, types, image_url, tags
        );
        recyclingService.write(writeDto);

        List<RecyclingInfo> savedInfos = recyclingRepository.findAll();
        assertNotNull(savedInfos);

    }

    @Test
    @DisplayName("분리배출 정보 검색 테스트")
    void searchResultDto_Test() {
        int count = 2;
        List<RecyclingDto.Search> infoList = Arrays.asList(
                new RecyclingDto.Search(1L, "Paper", new String[]{"Recycle"}, new String[]{"Recycle paper waste"}, new String[]{"종이"},
                        "paper_image.jpg", new String[]{"recycle", "paper"}, 0, LocalDate.now()),
                new RecyclingDto.Search(2L, "Plastic", new String[]{"Recycle"}, new String[]{"Recycle plastic waste"}, new String[]{"플라스틱"},
                        "plastic_image.jpg", new String[]{"recycle", "plastic"}, 0, LocalDate.now())
        );

        RecyclingDto.SearchResult searchResultDto = new RecyclingDto.SearchResult(count, infoList);

        assertEquals(count, searchResultDto.getCount());
        assertEquals(infoList, searchResultDto.getInfoList());
    }




}
