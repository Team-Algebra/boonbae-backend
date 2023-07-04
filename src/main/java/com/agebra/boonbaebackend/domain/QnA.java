package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="qna")
public class QnA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk")  //기본키
    private Long pk;

    @ManyToOne
    @JoinColumn(name="user_pk", referencedColumnName = "pk")
    private Users user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="qna_type")  //분류
    private QnAType qnaType;

    @NotNull
    @Column(name="title") //제목
    private String title;

    @NotNull
    @Lob
    @Column(name="descriptions",length = 999999999) //설명
    private String descriptions;

    @NotNull
    @CreationTimestamp
    @Column(name="create_at")
    private LocalDateTime createAt =LocalDateTime.now();

    @Column(name="reply_text", length=999999999) //답변이 달리면 Null이 아님 -> 답변완료
    private String replyText=null;

    public static QnA makeQnA(Users user, QnAType qnaType, String title, String description) {
        QnA qna= new QnA();
        qna.user = user;
        qna.qnaType = qnaType;
        qna.title = title;
        qna.descriptions = description;
        return qna;
    }
    public void editQnA(QnAType qnaType, String title, String description){
        this.qnaType=qnaType;
        this.title=title;
        this.descriptions=description;
    }
    public void makeReply(String replyText){
        this.replyText=replyText;
    }

    public void editReply(String replyText) {
            this.replyText = replyText;
    }

    public void deleteReply(){
        this.replyText=null;
    }
    public String getUserName(){
        return this.user.getNickname();
    }


}
