package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.RecyclingInfoTag;
import com.agebra.boonbaebackend.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecyclingInfoTagRepository extends JpaRepository<RecyclingInfoTag, Long> {

//    Tag findByName(String tag);

}
