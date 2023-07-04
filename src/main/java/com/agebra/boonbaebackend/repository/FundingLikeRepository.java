package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FundingLikeRepository extends JpaRepository<FundingLike, Long> {
    Optional<FundingLike> findByUserAndFunding(Users user, Funding funding);
    List<FundingLike> findByUser(Users user);

    int countByFunding(Funding funding);
    boolean existsByUserAndFunding(Users user, Funding funding);

}
