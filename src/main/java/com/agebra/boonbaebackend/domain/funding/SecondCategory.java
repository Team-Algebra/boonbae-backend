package com.agebra.boonbaebackend.domain.funding;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="second_category")
public class SecondCategory {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pk;

  @ManyToOne
  @JoinColumn(name = "first_category_pk", referencedColumnName = "pk")
  private FirstCategory firstCategory;

  @Column(name = "category_name")
  private String name;
}
