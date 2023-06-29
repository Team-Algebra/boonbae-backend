package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

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

    @NotNull
    @Column(name = "like_cnt")
    private int likeCnt = 0;

    @Lob
    @NotNull
    @Column(length = 999999999)
    private String content;

    @NotNull
    @Column(name = "report_cnt") //신고누적횟수
    private int reportCnt = 0;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDate createDate = LocalDate.now();

    public static Comment makeComment(String content) {
        Comment comment = new Comment();
        comment.content = content;
        return comment;
    }
}
