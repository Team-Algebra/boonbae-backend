package com.agebra.boonbaebackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @OneToMany(mappedBy = "recyclingInfo")
//    @JsonIgnoreProperties("recyclingInfo")
    private List<RecyclingInfoTag> RecycleTagList = new ArrayList<>();

    @NotNull
    @Column(name = "info_name")
    private String name;

    @Column(name = "image_url", length = 99999)
    private String imageUrl;

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

    public static RecyclingInfo makeRecyclingInfo(String name, TrashType type, String process, String description, String imageUrl)
    {
        RecyclingInfo recyclingInfo = new RecyclingInfo();
        recyclingInfo.name = name;
        recyclingInfo.type = type;
        recyclingInfo.process = process;
        recyclingInfo.description = description;
        recyclingInfo.imageUrl = imageUrl;
        return recyclingInfo;
    }

//    public void addTagList(List<Tag> tagList) {
//        this.tagList = tagList;
//    }

}
