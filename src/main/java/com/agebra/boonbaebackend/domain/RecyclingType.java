package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="recycling_type")
public class RecyclingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk")
    private Long pk;

    @NotNull
    @Column(name = "type_name")
    private String name;
}