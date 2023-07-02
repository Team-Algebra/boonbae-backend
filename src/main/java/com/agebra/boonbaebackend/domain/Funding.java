package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="funding")
public class Funding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk")  //기본키
    private Long pk;

//    @NotNull
//    @ManyToOne
//    @JoinColumn(name="user_pk", referencedColumnName = "pk")   등록한 사용자
//    private Users user;

    @NotNull
    @Column(name="title") //이름(제목)
    private String title;

    @NotNull
    @Column(name="first_category") //1차 카테고리
    private String firstCategory;

    @NotNull
    @Column(name="second_category") //2차 카테고리
    private String secondCategory;

    @Lob
    @NotNull
    @Column(name="introduction",length = 999999999) //소개
    private String introduction;

    @NotNull
    @Column(name="target_amount") //목표금액
    private int targetAmount;

    @NotNull
    @Column(name="current_amount")  //현재금액
    private int currentAmount;

    @NotNull
    @Column(name="supporting_amount")
    private int supportingAmount;

    @NotNull
    @Column(name="open_date")
    @CreationTimestamp
    @Builder.Default
    private LocalDate openDate=LocalDate.now();

    @NotNull
    @Column(name="close_date")
    private LocalDate closeDate;

    @Lob
    @NotNull
    @Column(name="main_img",length = 999999999)
    private String mainImg;

    @NotNull
    @Column(name="create_date")
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createDate= LocalDateTime.now();

    public static Funding makeFunding(String title, String firstCategory, String secondCategory,String introduction, int targetAmount, int currentAmount, int supportingAmount,LocalDate closeDate,String mainImg){
        Funding funding = new Funding();
        funding.title=title;
        funding.firstCategory=firstCategory;
        funding.secondCategory=secondCategory;
        funding.introduction=introduction;
        funding.targetAmount=targetAmount;
        funding.currentAmount=currentAmount;
        funding.supportingAmount=supportingAmount;
        funding.closeDate=closeDate;
        funding.mainImg=mainImg;
        return funding;
    }


}
