package com.agebra.boonbaebackend.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public class FundingDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class AddFunding {
    private String title;
    private Long target_amount;
    private Long supporting_amount;
    private Long second_category_pk;
    private String introduction;
    private LocalDate open_date;
    private LocalDate close_date;
    private String main_image;
  }


  @Getter
  @Builder
  @AllArgsConstructor
  public static class MyFunding{
    private Long funding_pk;
    private String title;
    private String first_category_name;
    private String second_category_name;
    private String owner_user_name;
    private String description;
    private Long current_amount;
    private Long target_amount;
    private String main_img;
    private Long DDay;
    private LocalDate open_date;
    private LocalDate close_date;
    private Long supporting_amount;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isLike = null;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MyFundingResult {
    private int count;
    private List<FundingDto.MyFunding> fundingList;
  }
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MyFundingResult_Page {
    private int PageCount;
    private Page<MyFunding> fundingList;
  }

}
