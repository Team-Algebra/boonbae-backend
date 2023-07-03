package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Funding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingRepository extends JpaRepository<Funding, Long> {
}
