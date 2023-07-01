package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Tree;
import com.agebra.boonbaebackend.domain.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findById(String id);
    Optional<Users> findByNickname(String nickName);

    @Query("SELECT u from Users u join u.tree")
    Optional<Users> findByPkWithTree(Long pk);

    @Query("SELECT u.tree from Users u join u.tree")
    List<Tree> getTreeByPk(Long pk);

    Long countBy();
}
