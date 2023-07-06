package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "user_pk", referencedColumnName = "pk")
    private Users user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "info_pk", referencedColumnName = "pk")
    private RecyclingInfo info;

    @Lob
    @NotNull
    @Column(length = 999999999)
    private String content;

    @NotNull
    @Column(name = "report_cnt") //신고누적횟수
    @Builder.Default private int reportCnt = 0;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    // 양방향 매핑
    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<CommentLike> commentLikeList = new HashSet<>();

}
