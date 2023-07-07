package com.agebra.boonbaebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TipDto {

  @Builder
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Tip_List {
    private Long tip_pk;
    private String name;
  }


}
