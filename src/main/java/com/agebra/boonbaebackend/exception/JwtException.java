package com.agebra.boonbaebackend.exception;

public class JwtException extends RuntimeException{
  public JwtException() {
    super();
  }

  public JwtException(String message) {
    super(message);
  }

  public JwtException(String message, Throwable cause) {
    super(message, cause);
  }

  public JwtException(Throwable cause) {
    super(cause);
  }

  protected JwtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
