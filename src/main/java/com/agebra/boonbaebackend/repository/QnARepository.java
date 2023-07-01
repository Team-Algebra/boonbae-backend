package com.agebra.boonbaebackend.repository;

import com.agebra.boonbaebackend.domain.QnA;
import com.agebra.boonbaebackend.domain.QnAType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA,Long> {
//    @Query("SELECT q FROM QnA q order by q.qnaType")
    List<QnA> findByQnaType(QnAType qnaType);
}
