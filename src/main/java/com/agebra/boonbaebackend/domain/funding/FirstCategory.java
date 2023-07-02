package com.agebra.boonbaebackend.domain.funding;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="first_category")
public class FirstCategory {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pk;

  @Column(name = "category_name")
  private String name;
}
