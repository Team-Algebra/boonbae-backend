package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Funding;
import com.agebra.boonbaebackend.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FundingRepository extends JpaRepository<Funding, Long> {
    // 현재 진행중인 해당 user의 펀딩을 최신순으로 조회
    @Query("SELECT f FROM Funding f WHERE f.closeDate >= CURRENT_DATE() ORDER BY f.createAt ASC")
    List<Funding> findOngoingFundingByUser(Users users);
    List<Funding> findByUser(Users user);

    @Query(value = "SELECT p FROM Funding p WHERE p.isApproved= :check")
    Page<Funding> findByApproved(Pageable pageable, boolean check);

    @Query(value = "SELECT p FROM Funding p WHERE p.isApproved= :check")
    List<Funding> findByApproved(boolean check, Sort sort);
}
