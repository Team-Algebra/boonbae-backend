package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByInfo_Pk(Long recyclingPk);
}
