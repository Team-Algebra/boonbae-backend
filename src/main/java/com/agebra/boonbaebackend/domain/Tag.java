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
    @Column(name="pk")
    private Long pk;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_pk", referencedColumnName = "pk")
    private RecyclingInfo info;

    @NotNull
    @Column(name = "tag_name")
    private String name;

    public static Tag makeTag(RecyclingInfo info, String name)
    {
        Tag tag = new Tag();
        tag.info = info;
        tag.name = name;
        return tag;
    }
}
