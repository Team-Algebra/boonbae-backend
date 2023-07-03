package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="tip")
public class Tip {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pk;

  @Column(length = 1000)
  private String content;

  public void modifyContent(String content) {
    this.content = content;
  }
}
