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
@Table(name="qna")
public class QnA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name="pk")  //기본키
    private Long pk;

    @NotNull
    @Column(name="user_pk")
    private Long userPk;

    @NotNull
    @Column(name="type")  //분류
    private QnAType qnaType;

    @NotNull
    @Column(name="title") //제목
    private String title;
    @NotNull
    @Lob
    @Column(name="description",length = 999999999) //설명
    private String description;

    @NotNull
    @Column(name="isreply")
    private boolean isReply = false;

    @NotNull
    @CreationTimestamp
    @Column(name="create_data")
    private LocalDate createDate=LocalDate.now();

    @Column(name="reply_text", length=999999999)
    private String replyText;

    public static QnA makeQnA(Long pk, Long userPk, QnAType qnAType, String title, String description, boolean isReply, LocalDate createDate, String replyText) {
        QnA qna= new QnA();
        qna.pk = pk;
        qna.userPk = userPk;
        qna.qnaType = qnAType;
        qna.title = title;
        qna.description = description;
        qna.isReply = isReply;
        qna.createDate = createDate;
        qna.replyText = replyText;
        return qna;
    }
}
