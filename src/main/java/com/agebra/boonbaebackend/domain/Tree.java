package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Builder
@Getter
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
  @Column(name = "accumulated_exp")
  private Long accumulatedExp = 0L;

  @NotNull
  @Builder.Default
  @Column(name = "recycle_cnt")
  private int recycleCnt = 0;

  @Builder.Default
  @NotNull
  @Column(name = "is_watching_ad")
  private boolean watchingAd = false; //광고봤는지 유무

  @NotNull
  @CreationTimestamp
  @Builder.Default
  @Column(name = "update_date")
  private LocalDate updateDate = LocalDate.now();

  @NotNull
  @Builder.Default
  @Column(name = "tonic_available")
  private int tonicAvailable = 3;

  public void init() {
    LocalDate current = LocalDate.now();

    if (current != updateDate) {
      watchingAd = false;
      tonicAvailable = 3;
    }
  }
}
