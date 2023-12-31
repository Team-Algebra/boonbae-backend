package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="funding_donate")
public class FundingDonate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "funding_pk", referencedColumnName = "pk")
    private Funding funding;

    @ManyToOne
    @JoinColumn(name = "user_pk", referencedColumnName = "pk")
    private Users user;

}
