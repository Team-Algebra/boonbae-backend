package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tree {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pk;

  @NotNull
  @Builder.Default
  private int exp = 0;

  @NotNull
  @Builder.Default
  @Column(name = "recycle_cnt")
  private int recycleCnt = 0;

  @Builder.Default
  private boolean ad = false; //광고봤는지 유무

  @NotNull
  @CreationTimestamp
  @Builder.Default
  @Column(name = "update_date")
  private LocalDate updateDate = LocalDate.now();

  @NotNull
  @Column(name = "tonic_cnt")
  @Builder.Default
  private int tonicCnt = 0;


}
