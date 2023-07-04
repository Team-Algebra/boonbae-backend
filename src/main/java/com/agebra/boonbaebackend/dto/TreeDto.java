package com.agebra.boonbaebackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TreeDto {
  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Info {
    private int current_exp;
//    private Long accumulated_exp;
    private Long all_cnt;
    private Long rank;
    private int recycle_cnt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int upload_available;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer eco_point;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean is_watching_ad;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer tonic_available;  //남은 영양제횟수

  }

  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Tier {
    private Long all_cnt;
    private Long rank;
  }

  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Confirm {
    private String image_url;

  }

  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CommonResponseDTO {
    private boolean success;
    private String reason;
  }
}
