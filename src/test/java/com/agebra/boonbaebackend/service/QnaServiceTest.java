package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.QnA;
import com.agebra.boonbaebackend.domain.QnAType;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.QnADto;
import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.exception.UserInfoDuplicatedException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QnaServiceTest {

  @Autowired
  private QnAService qnAService;

  @Autowired
  private UserService userService;

  @DisplayName("질문글 올리기")
  @Test
  @Transactional //rollback
  @Order(1)
  void addQna() {
    UserDto.RegisterRequest registerRequest = new UserDto.RegisterRequest("testId", "testNickName", "testPassword", "");
    Users user = userService.register(registerRequest);

    QnADto.Request qnaDto = new QnADto.Request("title", QnAType.ADD_REQUEST,"description");
    QnA writeQna = qnAService.write(qnaDto, user);

    QnADto.Response_oneQnA responseOneQnA = qnAService.one_QnA(writeQna.getPk());

    assertEquals("title", responseOneQnA.getTitle());
    assertEquals(QnAType.ADD_REQUEST, responseOneQnA.getQnaType());
    assertEquals("description", responseOneQnA.getDescription());
  }


  //질문글 삭제하기
  @DisplayName("질문글 삭제하기")
  @Test
  @Transactional //rollback
  @Order(2)
  void deleteQna() {
    UserDto.RegisterRequest registerRequest = new UserDto.RegisterRequest("testId", "testNickName", "testPassword", "");
    Users user = userService.register(registerRequest);

    QnADto.Request qnaDto = new QnADto.Request("title", QnAType.ADD_REQUEST,"description");
    QnA writeQna = qnAService.write(qnaDto, user);

    qnAService.delete(writeQna.getPk());

    //qna 삭제되어 조회 시 notfound exception이 발생해야함
    assertThrows(NotFoundException.class, () -> {
      qnAService.one_QnA(writeQna.getPk());
    });
  }

  //질문글 수정하기
  @DisplayName("질문글 수정하기")
  @Test
  @Transactional //rollback
  @Order(3)
  void modifyQna() {
    UserDto.RegisterRequest registerRequest = new UserDto.RegisterRequest("testId", "testNickName", "testPassword", "");
    Users user = userService.register(registerRequest);

    QnADto.Request qnaDto = new QnADto.Request("title", QnAType.ADD_REQUEST,"description");
    QnA writeQna = qnAService.write(qnaDto, user);

    String modifyTitle = "title1";
    String modifyDescription = "description1";

    QnADto.Request modifyQnaDto1 = new QnADto.Request(modifyTitle, QnAType.EDIT_REQUEST,modifyDescription);

    qnAService.update_QnA(writeQna.getPk(), modifyQnaDto1, user);
    QnADto.Response_oneQnA writeQnaDto = qnAService.one_QnA(writeQna.getPk());
    assertEquals(modifyTitle, writeQnaDto.getTitle());
    assertEquals(QnAType.EDIT_REQUEST, writeQnaDto.getQnaType());
    assertEquals(modifyDescription, writeQnaDto.getDescription());
    //qna 삭제되어 조회 시 notfound exception이 발생해야함
  }


  //질문글 조회하기 - 카테고리별로 조회/ 카테고리 아닌건 안나오게


  //답변달기

  //답변 수정하기




}
