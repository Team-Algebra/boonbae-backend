package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByName(String tag);

}
