package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.config.JwtAuthenticationFilter;
import com.agebra.boonbaebackend.dto.FundingDto;
import com.agebra.boonbaebackend.exception.CategoryDuplicateException;
import com.agebra.boonbaebackend.exception.GlobalExceptionHandler;
import com.agebra.boonbaebackend.service.CategoryService;
import com.agebra.boonbaebackend.service.FundingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(FundingController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("FundingControllerTest 테스트")
class FundingControllerTest {
    private MockMvc mvc;

    @MockBean
    private FundingService fundingService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new FundingController(fundingService, categoryService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // utf-8 필터 추가
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("펀딩 첫 번째 카테고리 이름이 중복된 것 추가 테스트")
    public void addDuplicateNameFirstCategory() throws Exception {
        //given
        doThrow(CategoryDuplicateException.class).when(categoryService).addFirstCategory(any());

        // when
        final ResultActions actions = mvc.perform(
                post("/api/v1/funding/category/first")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(
                                "{"
                                        + "  \"name\" : \"같은이름\""
                                        + "}"
                        )
        );

        // then
        actions.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("펀딩 두 번째 카테고리 이름이 중복된 것 추가 테스트")
    public void addDuplicateNameSecondCategory() throws Exception {
        //given
        doThrow(CategoryDuplicateException.class).when(categoryService).addSecondCategory(any());

        // when
        final ResultActions actions = mvc.perform(
                post("/api/v1/funding/category/second")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization ", "Bearer asdfa")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(
                                "{"
                                        + "\"first_category_id\" : \"1\","
                                        + "\"name\" : \"같은이름\""
                                        + "}"
                        )
        );

        // then
        actions.andExpect(status().isConflict());
    }


}