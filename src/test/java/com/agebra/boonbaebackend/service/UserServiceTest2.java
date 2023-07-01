package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.exception.UserInfoDuplicatedException;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //스프링부트 테스트 시 필요한 의존성 제공
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //클래스 단위로 테스트 진행 - 메소드별 각각 인스턴스 생성되지않음 -> 영향끼침. 순서지정필요
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //
public class UserServiceTest2 {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;

  private String nickName;
  private String id;
  private String password;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @BeforeAll
  void setUp() {
    nickName = "nick2";
    id = "idid2";
    password = "password";
  }

  @DisplayName("회원가입")
  @Test
  @Transactional //rollback
  @Order(1)
  void addUser() {
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);

    assertNotNull(user);
    assertNotNull(user.getPk());
    assertEquals(user.getId(), id);
    assertEquals(user.getNickname(), nickName);
    assertTrue(passwordEncoder.matches(password, user.getPassword()));
  }

  @DisplayName("중복아이디, 닉네임 회원가입 테스트")
  @Test
  @Transactional //rollback
  @Order(2)
  void isDuplicatedUserInfo() {
    //given
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);

    //when
    UserDto.RegisterRequest request2 = new UserDto.RegisterRequest(id, nickName+"1", password, "");
    UserDto.RegisterRequest request3 = new UserDto.RegisterRequest(id+"1", nickName, password, "");

    //then
    assertThrows(UserInfoDuplicatedException.class, () -> { //아이디 중복 테스트
      userService.register(request2);
    });
    assertThrows(UserInfoDuplicatedException.class, () -> { //닉네임 중복 테스트
      userService.register(request3);
    });
  }

  @DisplayName("로그인")
  @Test
  @Transactional //rollback
  @Order(3)
  void login() {
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);

    UserDto.Login requestLogin = new UserDto.Login(id, password);

    UserDto.LoginResponse response = userService.authenticate(requestLogin);
    assertNotNull(response.getToken());
    assertNotNull(response.getUser());
  }

  @DisplayName("닉네임 변경")
  @Test
  @Transactional //rollback
  @Order(4)
  void modifyNickname() {
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);

    userService.modifyUsername(user, nickName+"1");

    assertEquals(nickName + 1, user.getNickname());
  }

  @DisplayName("자기소개 변경")
  @Test
  @Transactional //rollback
  @Order(4)
  void modifyIntroduce() {
    UserDto.RegisterRequest request = new UserDto.RegisterRequest(id, nickName, password, "");
    Users user = userService.register(request);

    userService.modifyIntroduction(user, "asdf");

    String introduction = userService.getIntroduction(user);

    assertEquals(introduction, "asdf");
  }
}
