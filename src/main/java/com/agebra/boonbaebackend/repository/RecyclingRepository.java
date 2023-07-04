package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecyclingRepository extends JpaRepository<RecyclingInfo, Long> {
//    @Query("SELECT r FROM RecyclingInfo r join RecyclingInfoTag rit join rit.tag t WHERE r.name LIKE %:keyword% OR t.name LIKE %:keyword% OR CAST(r.type AS string) " +
//            "LIKE %:keyword% OR (:keyword LIKE '%종이%' AND (r.type = 'PAPER' OR r.type = 'PAPERPACK')) "
//            + "OR (:keyword LIKE '%페트%' AND r.type = 'PET') "
//            + "OR (:keyword LIKE '%플라스틱%' AND r.type = 'PLASTIC') "
//            + "OR (:keyword LIKE '%유리%' AND r.type = 'GLASS') "
//            + "OR (:keyword LIKE '%비닐%' AND r.type = 'VINYL') "
//            + "OR (:keyword LIKE '%캔%' AND r.type = 'CAN') "
//            + "OR (:keyword LIKE '%일반%' AND r.type = 'TRASH')")
    // 이름, 태그, type으로 분리배출 정보 검색

    @Query("SELECT ri FROM RecyclingInfo ri LEFT JOIN ri.RecycleTagList rit LEFT JOIN rit.tag t " +
            "LEFT JOIN ri.RecycleTypeList ritl LEFT JOIN ritl.type rt " +
            "WHERE ri.name LIKE %:keyword% OR t.name LIKE %:keyword% OR rt.name LIKE %:keyword%")
    List<RecyclingInfo> findByKeyword(String keyword);

    List<RecyclingInfo> findTop5ByOrderByViewCntDesc();
}
