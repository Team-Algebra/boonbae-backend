package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="comments_like",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_pk", "comment_pk"})}) // user_pk와 comment_pk의 조합이 unique해야 함
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_pk", referencedColumnName = "pk")
    private Users user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "comment_pk", referencedColumnName = "pk")
    private Comment comment;
}
