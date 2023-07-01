package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Comment;
import com.agebra.boonbaebackend.domain.CommentLike;
import com.agebra.boonbaebackend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(Users user, Comment comment);

    int countByComment(Comment comment);
    boolean existsByUserAndComment(Users user, Comment comment);
}
