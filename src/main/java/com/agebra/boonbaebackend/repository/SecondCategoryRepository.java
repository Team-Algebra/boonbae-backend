package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.funding.FirstCategory;
import com.agebra.boonbaebackend.domain.funding.SecondCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecondCategoryRepository extends JpaRepository<SecondCategory, Long> {

  Optional<SecondCategory> findByName(String name);

  Optional<SecondCategory> findByFirstCategoryAndName(FirstCategory firstCategory, String name);
  List<SecondCategory> findAllByFirstCategory(FirstCategory firstCategory);
}
