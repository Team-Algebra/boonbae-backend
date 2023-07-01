package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecyclingRepository extends JpaRepository<RecyclingInfo, Long> {

    // 이름, 태그, type으로 분리배출 정보 검색
    @Query("SELECT r FROM RecyclingInfo r LEFT JOIN r.tagList t WHERE r.name LIKE %:keyword% OR t.name LIKE %:keyword% OR CAST(r.type AS string) LIKE %:keyword%")
    List<RecyclingInfo> findByKeyword(String keyword);

    @Modifying
    @Query("UPDATE RecyclingInfo r SET r.viewCnt = r.viewCnt + 1 WHERE r.pk = :pk")
    void updateViewCount(Long pk);

    List<RecyclingInfo> findTop5ByOrderByViewCntDesc();

}
