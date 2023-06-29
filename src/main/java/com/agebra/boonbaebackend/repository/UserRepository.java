package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findById(String id);
    Optional<Users> findByNickname(String nickName);
}
