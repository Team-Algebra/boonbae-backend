package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Funding;
import com.agebra.boonbaebackend.domain.FundingLike;
import com.agebra.boonbaebackend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundingRepository extends JpaRepository<Funding, Long> {
    List<Funding> findByUser(Users user);
}
