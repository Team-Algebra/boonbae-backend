package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.RecyclingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RecyclingTypeRepository extends JpaRepository<RecyclingType, Long> {

    Optional<List<RecyclingType>> findByName(String type);

}
