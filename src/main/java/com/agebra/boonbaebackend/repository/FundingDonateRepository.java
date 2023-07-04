package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Funding;
import com.agebra.boonbaebackend.domain.FundingDonateList;
import com.agebra.boonbaebackend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundingDonateRepository extends JpaRepository<FundingDonateList, Long> {
    boolean existsByUserAndFunding(Users user, Funding funding);
    List<FundingDonateList> findByUser(Users user);
}
