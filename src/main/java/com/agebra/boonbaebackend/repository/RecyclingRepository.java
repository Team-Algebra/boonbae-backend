package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecyclingRepository extends JpaRepository<RecyclingInfo, Long> {

    // 이름, 태그, type으로 분리배출 정보 검색
    @Query("SELECT r FROM RecyclingInfo r LEFT JOIN r.tagList t WHERE r.name LIKE %:keyword% OR t.name LIKE %:keyword% OR CAST(r.type AS string) " +
            "LIKE %:keyword% OR (:keyword LIKE '%종이%' AND (r.type = 'PAPER' OR r.type = 'PAPERPACK')) "
            + "OR (:keyword LIKE '%페트%' AND r.type = 'PET') "
            + "OR (:keyword LIKE '%플라스틱%' AND r.type = 'PLASTIC') "
            + "OR (:keyword LIKE '%유리%' AND r.type = 'GLASS') "
            + "OR (:keyword LIKE '%비닐%' AND r.type = 'VINYL') "
            + "OR (:keyword LIKE '%캔%' AND r.type = 'CAN') "
            + "OR (:keyword LIKE '%일반%' AND r.type = 'TRASH')")
    List<RecyclingInfo> findByKeyword(String keyword);

}
