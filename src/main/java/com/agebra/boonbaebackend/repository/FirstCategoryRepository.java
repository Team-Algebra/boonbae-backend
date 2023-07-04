package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.funding.FirstCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FirstCategoryRepository extends JpaRepository<FirstCategory, Long> {

  Optional<FirstCategory> findByName(String name);
}
