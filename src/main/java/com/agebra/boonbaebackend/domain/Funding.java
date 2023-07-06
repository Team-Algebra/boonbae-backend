package com.agebra.boonbaebackend.domain;

import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
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

    @ManyToOne
    @JoinColumn(name="user_pk", referencedColumnName = "pk")   // 등록한 사용자
    private Users user;

    @NotNull
    @Column(name="title") //이름(제목)
    private String title;

    @ManyToOne
    @JoinColumn(name="second_category", referencedColumnName = "pk") //2차 카테고리
    private SecondCategory category;

    @Lob
    @NotNull
    @Column(name="content",length = 999999999) //펀딩아이템 소개
    private String content;

    @NotNull
    @Column(name="target_amount") //목표금액
    private Long targetAmount;

    @NotNull
    @Builder.Default
    @Column(name="current_amount")  //현재금액
    private Long currentAmount = 0L;

    @NotNull
    @Column(name="supporting_amount") //1회 후원금액
    private Long supportingAmount;

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
    @Builder.Default
    @Column(name="is_approved")
    private boolean isApproved = false;

    @NotNull
    @Column(name="create_at")
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();

    public static Funding makeFunding(String title, SecondCategory category, String content, Long targetAmount, Long supportingAmount,LocalDate closeDate,String mainImg){
        Funding funding = new Funding();
        funding.title=title;
        funding.category = category;
        funding.content=content;
        funding.targetAmount=targetAmount;
        funding.supportingAmount=supportingAmount;
        funding.closeDate=closeDate;
        funding.mainImg=mainImg;
        return funding;
    }

    public void addCurrentAmount(){
        this.currentAmount+=supportingAmount;
    }

    public Long getDDay(){
        LocalDateTime date1 = this.getOpenDate().atStartOfDay();
        LocalDateTime date2 = this.getCloseDate().atStartOfDay();
        int dday = (int) Duration.between(date1,date2).toDays();
        Long betweenDays=Long.valueOf(dday);
        return betweenDays;
    }

    //펀딩승인
    public void accessFunding(){
        this.isApproved=true;
    }
    
}
