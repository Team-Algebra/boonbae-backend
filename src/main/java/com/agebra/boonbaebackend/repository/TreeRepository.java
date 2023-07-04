package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TreeRepository extends JpaRepository<Tree, Long> {

  @Query("SELECT SUM(t.exp) FROM Tree t")
  Optional<Long> getAllExp();

  @Query(value = "SELECT COUNT(*) + 1 FROM tree WHERE exp > :exp", nativeQuery = true)
  Long findRankByExp(int exp);

}
