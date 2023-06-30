package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String tag);

}
