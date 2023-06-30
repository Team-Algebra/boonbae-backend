package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TreeRepository extends JpaRepository<Tree, Long> {

  String findRank = """
    SELECT t1.pk, t1.exp, COUNT(DISTINCT t2.exp) + 1 AS ranking
    FROM tree t1
    LEFT JOIN tree t2 ON t1.exp < t2.exp
    WHERE t1.pk = :pk
    GROUP BY t1.pk, t1.exp
    """;

  @Query("SELECT SUM(t.exp) FROM Tree t")
  Optional<Long> getAllExp();

  @Query(value = findRank, nativeQuery = true)
  Long findRankById(@Param("pk") Long pk);

}
