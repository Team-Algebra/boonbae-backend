package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.RecycleConfirm;
import com.agebra.boonbaebackend.domain.Tree;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.TreeDto;
import com.agebra.boonbaebackend.dto.UserDto;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TreeServiceTest {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;

  @Autowired
  private TreeService treeService;

  @Autowired
  private ValueService valueService;

  @Autowired
  private RecycleConfirmService recycleConfirmService;

  private String nickName;
  private String id;
  private String password;

  @BeforeAll
  void setUp() {
    nickName = "nick";
    id = "idid";
    password = "password";
  }

  @DisplayName("모든 나무 경험치 합산: 초기화")
  @Transactional //rollback
  @Order(1)
  @Test
  void getAllExpZero() {
    //given
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);
    UserDto.RegisterRequest request2 = new UserDto.RegisterRequest(id+"1", nickName+"1", password, "");
    Users user2 = userService.register(request2);

    assertEquals(0L, treeService.getAllExp());
  }

  @DisplayName("분리배출 업로드 시 - 업로드 가능 횟수 까이는지")
  @Transactional //rollback
  @Order(2)
  @Test
  void uploadRecycle() {
    //given
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);
    UserDto.RegisterRequest request2 = new UserDto.RegisterRequest(id+"1", nickName+"1", password, "");
    Users user2 = userService.register(request2);

    //user가 2번 올림
    RecycleConfirm recycleConfirm = treeService.uploadRecycle(user, new TreeDto.Confirm("image_url"));
    RecycleConfirm recycleConfirm2 = treeService.uploadRecycle(user, new TreeDto.Confirm("image_url"));
    //user2가 1번 올림
    RecycleConfirm recycleConfirm3 = treeService.uploadRecycle(user2, new TreeDto.Confirm("image_url"));

    //then
    Tree tree1 = user.getTree();
    Tree tree2 = user2.getTree();
    assertEquals(1, tree1.getUploadAvailable());
    assertEquals(2, tree2.getUploadAvailable());
  }

  @DisplayName("분리배출 인증 pass 했을 때 결과값(point, exp, accumulate exp 등)")
  @Transactional //rollback
  @Order(3)
  @Test
  void passResultTest() {
    //given
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);
    UserDto.RegisterRequest request2 = new UserDto.RegisterRequest(id+"1", nickName+"1", password, "");
    Users user2 = userService.register(request2);

    RecycleConfirm recycleConfirm = treeService.uploadRecycle(user, new TreeDto.Confirm("asdf"));
    RecycleConfirm recycleConfirm2 = treeService.uploadRecycle(user, new TreeDto.Confirm("asdf"));
    recycleConfirmService.pass(recycleConfirm.getPk());
    recycleConfirmService.pass(recycleConfirm2.getPk());
    
    RecycleConfirm recycleConfirm3 = treeService.uploadRecycle(user2, new TreeDto.Confirm("asdf"));
    recycleConfirmService.pass(recycleConfirm3.getPk());

    //then
    Tree tree1 = user.getTree();
    Tree tree2 = user2.getTree();

    //누적포인트 테스트
    assertEquals(valueService.getRecyclePoint() * 3, user.getEcoPoint() + user2.getEcoPoint());

    //분리배출횟수 증가 여부 테스트
    assertEquals(2, tree1.getRecycleCnt());
    assertEquals(1, tree2.getRecycleCnt());

    //경험치 증가여부 테스트
    assertEquals(valueService.getRecycleExp()*2, tree1.getExp());
    assertEquals(valueService.getRecycleExp(), tree2.getExp());

    //누적 경험치 증가여부 테스트
    assertEquals(valueService.getRecycleExp()*2, tree1.getAccumulatedExp());
    assertEquals(valueService.getRecycleExp(), tree2.getAccumulatedExp());

    //포인트 증가여부 테스트
    assertEquals(valueService.getRecyclePoint()*2, user.getEcoPoint());
    assertEquals(valueService.getRecyclePoint(), user2.getEcoPoint());
  }

  @DisplayName("분리배출 3번이상 불가능")
  @Transactional //rollback
  @Order(5)
  @Test
  void cantThreeTime() {



  }

  @DisplayName("다음날로 넘어갔을 때 초기화가 되는지")
  @Transactional //rollback
  @Order(6)
  @Test
  void initDayChanged() {

  }

  @Test
  void getUsersTreeInfo() {




  }


}