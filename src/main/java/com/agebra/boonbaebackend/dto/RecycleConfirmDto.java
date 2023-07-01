package com.agebra.boonbaebackend.dto;

import com.agebra.boonbaebackend.domain.RecycleConfirm;
import com.agebra.boonbaebackend.domain.RecycleConfirmStatus;
import lombok.*;

import java.util.List;

public class RecycleConfirmDto {
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Info {
    private int total_page;
    private List<Detail> list;

    @AllArgsConstructor
    @Data
    public static class Detail {
      private Long recycle_confirm_pk;
      private Long user_pk;
      private String username;
      private RecycleConfirmStatus status;
    }
  }
}
