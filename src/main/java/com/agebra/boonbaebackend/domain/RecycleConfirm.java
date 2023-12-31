package com.agebra.boonbaebackend.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecycleConfirm {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pk;

  @ManyToOne
  @JoinColumn(name = "user_pk", referencedColumnName = "pk")
  private Users user;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "confirm_status")
  @Builder.Default
  private RecycleConfirmStatus status = RecycleConfirmStatus.WAITING;

  @NotNull
  @Column(name = "image_url", length = 99999)
  private String imageUrl;

  @NotNull
  @CreationTimestamp
  @Builder.Default
  @Column(name="create_at")
  private LocalDateTime createAt =LocalDateTime.now();

  public void pass() {
    this.status = RecycleConfirmStatus.PASS;
  }

  public void unpass() {
    this.status = RecycleConfirmStatus.UNPASS;
  }
}
