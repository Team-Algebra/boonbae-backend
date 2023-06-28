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
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    @Column(name = "like_cnt")
    private int likeCnt;

    @Lob
    @NotNull
    @Column(length = 999999999)
    private String content;

    @NotNull
    @Column(name = "report_cnt")
    private int reportCnt;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDate createDate = LocalDate.now();

    public static Comment makeComment(String content) {
        Comment comment = new Comment();
        comment.likeCnt = 0;
        comment.content = content;
        comment.reportCnt = 0;
        return comment;
    }
}
