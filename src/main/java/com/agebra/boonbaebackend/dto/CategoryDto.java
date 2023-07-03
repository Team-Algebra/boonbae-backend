package com.agebra.boonbaebackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  public static class All {
    @NotNull
    private Long first_category_id;

    @NotNull
    private String name;
  }

}
