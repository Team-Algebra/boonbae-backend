package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.QnA;
import com.agebra.boonbaebackend.domain.QnAType;
import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.repository.QnARepository;
import com.agebra.boonbaebackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class QnAServiceTest{
  @Mock
  private QnARepository qnaRepository;

  @InjectMocks
  private QnAService qnaService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("검색 확인테스트")
  void searchQnAType_Test() {
    Users user1 = Users.builder()
      .id("id1")
      .password("pass1")
      .nickname("nick1")
      .build();

    QnA qna1 = QnA.makeQnA(
      user1, QnAType.ADD_REQUEST,"test1","testde");
    QnA qna2 = QnA.makeQnA(
      user1, QnAType.ADD_REQUEST,"test1","testde");
    QnA qna3 = QnA.makeQnA(
      user1, QnAType.ETC,"test1","testde");
    List<QnA> mockQnAList = new ArrayList<>();
    mockQnAList.add(qna1);
    mockQnAList.add(qna2);
    when(qnaRepository.findByQnaType(QnAType.ADD_REQUEST)).thenReturn(mockQnAList);
    Pageable pageable= PageRequest.of(1, 5);
    List<QnA> qnaList = qnaRepository.findByQnaType(QnAType.ADD_REQUEST);
    assertEquals(mockQnAList,qnaList);

  }
}