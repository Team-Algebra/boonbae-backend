package com.agebra.boonbaebackend.dto;

import com.agebra.boonbaebackend.domain.TrashType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecyclingDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Write {
    @NotNull(message = "분리배출 정보 name이 null이면 안됩니다")
    private String name;

    @NotNull(message = "분리배출 정보 버리는방법이 null이면 안됩니다")
    private String process; //domain에서는 nickname임

    @NotNull(message = "분리배출 정보 설명이 null이면 안됩니다")
    private String description;

    @NotNull(message = "분리배출 정보 타입이 null이면 안됩니다")
    private TrashType type;

    private String image_url;
    private String[] tags;

  }

}
