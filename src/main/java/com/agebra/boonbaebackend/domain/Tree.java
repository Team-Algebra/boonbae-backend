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

  @NotNull
  @Builder.Default
  @Column(name = "upload_available")
  private int uploadAvailable = 3;

  public void initAll() {
    LocalDate current = LocalDate.now();

    //업데이트 날짜보다 현재 날짜가 더 크면
    if (current.isAfter(updateDate)) {
      watchingAd = false;
      tonicAvailable = 3;
      uploadAvailable = 3;
      updateDate = current;
    }
  }

  public boolean isExceedUploadCnt() {
    return (uploadAvailable <= 0);
  }

  public boolean isExceedTonicCnt(int expectedDecrement) {
    return (tonicAvailable - expectedDecrement < 0);
  }

  public boolean isExceedWatchingAd() {
    return (watchingAd);
  }


  public void recycleComplete() {
    initAll();
    uploadAvailable -= 1;
  }

  public void useTonic(int amount, int perExp) {
    initAll();
    tonicAvailable -= amount;
    exp += perExp;
    accumulatedExp += perExp;

  }

  public void watchAd(int perExp) {
    initAll();
    watchingAd = true;
    exp += perExp;
    accumulatedExp += perExp;
  }

  //관리자전용 - 분리배출 인증완료
  public void recyclePass(int perExp) {
    exp += perExp;
    accumulatedExp += perExp;
    recycleCnt ++;
  }
}
