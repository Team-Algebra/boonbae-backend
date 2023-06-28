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
@Table(name="recycling_info")
public class RecyclingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    @Column(name = "info_name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recycle_type")
    private TrashType type;

    @NotNull
    @Column(name="recycle_process", length = 99999)
    private String process;

    @NotNull
    @Column(name="descriptions", length = 99999)
    private String description;

    @NotNull
    @Column(name="view_cnt", columnDefinition = "int default 0")
    private int viewCnt = 0;

    @NotNull
    @CreationTimestamp
    @Column(name="create_date")
    private LocalDate createDate = LocalDate.now();

    public static RecyclingInfo makeRecyclingInfo(String name, TrashType type, String process, String description, int viewCnt)
    {
        RecyclingInfo recyclingInfo = new RecyclingInfo();
        recyclingInfo.name = name;
        recyclingInfo.type = type;
        recyclingInfo.process = process;
        recyclingInfo.description = description;
        recyclingInfo.viewCnt = viewCnt;
        return recyclingInfo;
    }

}
