package com.deliveroo.parser.exceptions;

public class CronParsingException extends RuntimeException {
  public CronParsingException(String errorMessage) {
    super(errorMessage);
  }
}
