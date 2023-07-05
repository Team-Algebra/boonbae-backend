package com.agebra.boonbaebackend.service;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class ValueService {

  //분리수거 인증 시 지급되는 포인트량

  private int referralPoint = 10;

  private int recycleExp = 10;
  private int recyclePoint = 5;

  private int tonicExp = 30;
  private int tonicPoint = -15;
  private int tonicWonPrice = 1500;

  private int advertisementExp = 30;




}
