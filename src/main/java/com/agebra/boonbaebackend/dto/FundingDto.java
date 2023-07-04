package com.agebra.boonbaebackend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class FundingDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
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
  public static class MyFundingResponse {
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
  }

}
