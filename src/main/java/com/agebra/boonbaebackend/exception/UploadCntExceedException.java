package com.agebra.boonbaebackend.exception;

public class UploadCntExceedException extends RuntimeException{
  public UploadCntExceedException() {
    super();
  }

  public UploadCntExceedException(String message) {
    super(message);
  }

  public UploadCntExceedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UploadCntExceedException(Throwable cause) {
    super(cause);
  }

  protected UploadCntExceedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
