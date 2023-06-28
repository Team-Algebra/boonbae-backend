package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name="pk")
    private Long pk;

    @NotNull
    @Column(name="info_pk")
    private Long infoPk;

    @NotNull
    private String name;

    public static Tag makeTag(Long infoPk, String name)
    {
        Tag tag = new Tag();
        tag.infoPk = infoPk;
        tag.name = name;
        return tag;
    }

}
