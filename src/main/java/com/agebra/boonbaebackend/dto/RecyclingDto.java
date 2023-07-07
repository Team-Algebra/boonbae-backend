package com.agebra.boonbaebackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

public class RecyclingDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Write {
    @NotNull(message = "분리배출 정보 name이 null이면 안됩니다")
    private String name;

    @NotNull(message = "분리배출 정보 버리는방법이 null이면 안됩니다")
    private String[] process; //domain에서는 nickname임

    @NotNull(message = "분리배출 정보 설명이 null이면 안됩니다")
    private String[] description;

    @NotNull(message = "분리배출 정보 타입이 null이면 안됩니다")
    private String[] types;

    private String image_url;
    private String[] tags;

  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SearchResult {
    private int count;
    private List<Search> infoList;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Search {
    private Long pk;
    private String name;
    private String[] process;
    private String[] description;
    private String[] types;
    private String imageUrl;
    private String[] tags;
    private int viewCnt;
    private LocalDate createDate;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DetailResult {
    private String name;
    private String[] process;
    private String[] description;
    private String[] types;
    private String imageUrl;
    private String[] tags;
    private int viewCnt;
    private LocalDate createDate;
  }




}
