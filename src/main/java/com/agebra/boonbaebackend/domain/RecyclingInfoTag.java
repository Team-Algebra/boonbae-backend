package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="recycling_info_tag")
public class RecyclingInfoTag {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pk;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recycling_info_pk", referencedColumnName = "pk")
  private RecyclingInfo recyclingInfo;

  @ManyToOne
  @JoinColumn(name = "tag_pk", referencedColumnName = "pk")
  private Tag tag;
}
