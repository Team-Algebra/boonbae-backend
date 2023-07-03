package com.agebra.boonbaebackend.dto;


import com.agebra.boonbaebackend.domain.funding.FirstCategory;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class FundingDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Add {
    private String title;
    private Long target_amount;
    private Long supporting_amount;
    private Long second_category_pk;
    private String introduction;
    private LocalDate open_date;
    private LocalDate close_date;
    private String main_image;
  }

}
