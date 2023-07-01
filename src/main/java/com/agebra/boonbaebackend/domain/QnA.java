package com.agebra.boonbaebackend.domain;

import com.agebra.boonbaebackend.exception.ForbiddenException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;

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
    @Column(name="create_data")
    private LocalDateTime createDate=LocalDateTime.now();

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
    public void editQnA(Users user, QnAType qnaType, String title, String description){
        if(this.user.getPk()==user.getPk()){
            this.qnaType=qnaType;
            this.title=title;
            this.descriptions=description;
        }else{
            throw new ForbiddenException("QnA를 작성한 유저가 아닙니다.");
        }
    }
    public void makeReply(String replyText){
        this.replyText=replyText;
    }

    public void editReply(String replyText) {
        if (this.replyText == null) {
            throw new ForbiddenException("답변을 먼저 작성해주세요");

        } else {
            this.replyText = replyText;
        }
    }
    public String getUserName(){
        return this.user.getUsername();
    }


}
