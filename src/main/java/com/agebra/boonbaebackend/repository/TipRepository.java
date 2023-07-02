package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TipRepository extends JpaRepository<Tip, Long> {

  @Query(value = "SELECT t.content FROM Tip t WHERE t.id >= (SELECT FLOOR(MAX(e2.id) * RAND()) FROM YourEntity e2) ORDER BY e.id LIMIT 1", nativeQuery = true)
  String findRandomColumnName();

  @Query("SELECT t.content from Tip t ORDER BY RAND() LIMIT 1")
  Optional<String> getTipRandom();

  //성능향상?
  //SELECT column_name
  //FROM table_name AS t1
  //JOIN (SELECT ROUND(RAND() * (SELECT MAX(id) FROM table_name)) AS rand_id) AS t2
  //WHERE t1.id >= t2.rand_id
  //ORDER BY t1.id
  //LIMIT 1;
}
