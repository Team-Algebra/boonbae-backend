package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.UserDto;
import com.agebra.boonbaebackend.exception.ForbiddenException;
import com.agebra.boonbaebackend.exception.UserInfoDuplicatedException;
import com.agebra.boonbaebackend.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ValueService valueService;


    public Users register(UserDto.RegisterRequest dto) throws RuntimeException {
        if (isExistId(dto.getId()))
            throw new UserInfoDuplicatedException("user의 아이디가 중복됩니다");

        if (isExistNickname(dto.getUsername()))
            throw new UserInfoDuplicatedException("user의 닉네임이 중복됩니다");

        Users referrerUser = userRepository.findById(dto.getRefferer_id())
          .orElseGet(() -> null);

        if (referrerUser != null) { //추천인이 존재하면 해당 유저에게 포인트 지급
            referrerUser.addReferralPoint(valueService.getReferralPoint());
        }

//        Users user = Users.makeUser(
//          dto.getId(),
//          passwordEncoder.encode(dto.getPassword()),
//          dto.getUsername()
//        );

        Users user = Users.builder()
            .id(dto.getId())
            .password(passwordEncoder.encode(dto.getPassword()))
            .nickname(dto.getUsername())
            .build();

        userRepository.save(user);

        return user;
    }

    public UserDto.LoginResponse authenticate(UserDto.Login dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getId(),
                        dto.getPassword()
                )
        );

        Users user = userRepository.findById(dto.getId())
          .orElseThrow(() -> new NoSuchElementException());

        String jwtToken = jwtService.generateToken(user);
        return new UserDto.LoginResponse(jwtToken, user.getId(), user.getNickname());
    }

    //중복 아이디 확인
    @Transactional(readOnly = true)
    public boolean isExistId(String id) {
        Users user = userRepository.findById(id)
          .orElseGet(() -> null);

        return (user == null)? false : true;
    }

    //중복 닉네임 확인
    @Transactional(readOnly = true)
    public boolean isExistNickname(String nickName) {
        Users user = userRepository.findByNickname(nickName)
          .orElseGet(() -> null);

        return (user == null)? false : true;
    }

    public void delete(Users user) {
        userRepository.delete(user);
    }

    public void modifyUsername(Users user, String nickName) throws UserInfoDuplicatedException {
        Users findUsers = userRepository.findById(user.getPk())
          .orElseThrow(() -> new ForbiddenException("일치하는 사용자가 없습니다"));

        if (isExistNickname(nickName))
            throw new UserInfoDuplicatedException();

        findUsers.changeNickname(nickName);
    }


    public void chargePoint(Users user, int amount) {
        Users findUser = userRepository.findById(user.getPk())
          .orElseThrow(() -> new NoSuchElementException());

        findUser.chargePoint(amount);
    }

    public String getIntroduction(Users user) {
        Users findUser = userRepository.findById(user.getPk())
          .orElseThrow(() -> new NoSuchElementException());

        if (user.getIntroduction() == null)
            return "";
        else
            return findUser.getIntroduction();
    }

    public void modifyIntroduction(Users user, String introduction) {
        Users findUser = userRepository.findById(user.getPk())
          .orElseThrow(() -> new NoSuchElementException());

        findUser.changeIntroduction(introduction);
    }
}
