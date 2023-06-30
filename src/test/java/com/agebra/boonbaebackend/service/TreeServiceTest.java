package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.UserDto;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  private String nickName;
  private String id;
  private String password;

  @BeforeAll
  void setUp() {
    nickName = "nick";
    id = "idid";
    password = "password";
  }

  @DisplayName("모든 나무 경험치 합산")
  @Transactional //rollback
  @Order(1)
  @Test
  void getAllExp() {
    //given
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);
    UserDto.RegisterRequest request2 = new UserDto.RegisterRequest(id+"1", nickName+"1", password, "");
    Users user2 = userService.register(request2);

    assertEquals(0L, treeService.getAllExp());
  }

  @Test
  void getUsersTreeInfo() {
  }

  @Test
  void getMyTreeInfo() {
  }
}