package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.RecyclingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RecyclingTypeRepository extends JpaRepository<RecyclingType, Long> {

    Optional<RecyclingType> findByName(String type);

}
