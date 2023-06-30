package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.RecyclingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecyclingRepository extends JpaRepository<RecyclingInfo, Long> {

    @Query("SELECT r FROM RecyclingInfo r WHERE r.name LIKE %:keyword%")
    List<RecyclingInfo> findByKeyword(String keyword);


}
