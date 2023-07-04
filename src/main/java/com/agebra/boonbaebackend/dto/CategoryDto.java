package com.agebra.boonbaebackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class CategoryDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Add {
    @NotNull
    private Long first_category_id;

    @NotNull
    private String name;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class All {
    @NotNull
    private int first_category_cnt;

    @NotNull
    private List<FirstCategoryDto> first_category;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FirstCategoryDto {
    @NotNull
    private int second_category_cnt;

    @NotNull
    private String name;

    @NotNull
    private List<SecondCategoryDto> second_category;


  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SecondCategoryDto {
    @NotNull
    private Long second_category_pk;

    @NotNull
    private String name;

  }

}
