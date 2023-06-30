package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("중복아이디 여부 확인 테스트")
  void isExistsId_Test() {
//    Users user1 = Users.makeUser("id1", "pass1", "nick1");

    Users user1 = Users.builder()
        .id("id1")
          .password("pass1")
            .nickname("nick1")
              .build();

    when(userRepository.findById("id1")).thenReturn(Optional.of(user1));

    boolean isExistId = userService.isExistId("id1");

    verify(userRepository, times(1)).findById("id1");

    assertEquals(true, isExistId);
  }
}