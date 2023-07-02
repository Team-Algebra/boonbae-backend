package com.agebra.boonbaebackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler({UserInfoDuplicatedException.class})
  protected ResponseEntity handleUserInfoDuplicatedException(UserInfoDuplicatedException e) {
    log.info("UserInfoDuplicatedException = {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler({InputMismatchException.class})
  protected ResponseEntity handleInputMismatchException(InputMismatchException e) {
    log.info("InputMismatchException = {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler({NoSuchUserException.class})
  protected ResponseEntity handleNoSuchUserException(NoSuchUserException e) {
    log.info("NoSuchUserException = {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ExceptionHandler({ForbiddenException.class})
  protected ResponseEntity handleInputMismatchException(ForbiddenException e) {
    log.info("ForbiddenException = {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @ExceptionHandler({UploadCntExceedException.class})
  protected ResponseEntity handleUploadCntExceedException(UploadCntExceedException e) {
    log.info("UploadCntExceedException = {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
  }

  @ExceptionHandler({NotFoundException.class})
  protected ResponseEntity handleNotFoundException(NotFoundException e) {
    log.info("NotFoundException = {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }
}
