package com.agebra.boonbaebackend.service;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class ValueService {

  //분리수거 인증 시 지급되는 포인트량
  private int addPoint;


}
