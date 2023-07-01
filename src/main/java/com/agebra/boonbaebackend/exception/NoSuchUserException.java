package com.agebra.boonbaebackend.exception;

public class NoSuchUserException extends RuntimeException{
  public NoSuchUserException() {
    super();
  }

  public NoSuchUserException(String message) {
    super(message);
  }

  public NoSuchUserException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSuchUserException(Throwable cause) {
    super(cause);
  }

  protected NoSuchUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
