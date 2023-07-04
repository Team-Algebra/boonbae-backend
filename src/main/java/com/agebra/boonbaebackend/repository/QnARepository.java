package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.QnA;
import com.agebra.boonbaebackend.domain.QnAType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA,Long> {
  //    @Query("SELECT q FROM QnA q order by q.qnaType")
  Page<QnA> findByQnaType(QnAType qnaType, Pageable pageable);
  List<QnA> findByQnaType(QnAType qnaType);
  List<QnA> findAll(Sort sort);
//  @Query(value = "SELECT p FROM QnA p WHERE p.replyText != :null")
  List<QnA> findAllByReplyTextIsNotNull();

}