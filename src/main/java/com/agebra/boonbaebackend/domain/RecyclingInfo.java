package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="recycling_info")
public class RecyclingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Builder.Default
    @OneToMany(mappedBy = "recyclingInfo")
//    @JsonIgnoreProperties("recyclingInfo")
    private List<RecyclingInfoTag> RecycleTagList = new ArrayList<>();

    @NotNull
    @Column(name = "info_name")
    private String name;

    @Column(name = "image_url", length = 99999)
    private String imageUrl;

    @Builder.Default
    @OneToMany(mappedBy = "recyclingInfo")
    private List<RecyclingInfoType> RecycleTypeList = new ArrayList<>();

    @NotNull
    @Column(name="recycle_process", length = 99999)
    private String process;

    @NotNull
    @Column(name="descriptions", length = 99999)
    private String description;

    @Builder.Default
    @NotNull
    @Column(name="view_cnt", columnDefinition = "int default 0")
    private int viewCnt = 0;

    @Builder.Default
    @NotNull
    @CreationTimestamp
    @Column(name="create_date")
    private LocalDate createDate = LocalDate.now();

    public static RecyclingInfo makeRecyclingInfo(String name, String process, String description, String imageUrl)
    {
        RecyclingInfo recyclingInfo = new RecyclingInfo();
        recyclingInfo.name = name;
        recyclingInfo.process = process;
        recyclingInfo.description = description;
        recyclingInfo.imageUrl = imageUrl;
        return recyclingInfo;
    }

//    public void addTagList(List<Tag> tagList) {
//        this.tagList = tagList;
//    }

}
