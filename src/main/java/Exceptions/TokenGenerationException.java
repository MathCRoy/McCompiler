package Exceptions;

public class TokenGenerationException extends RuntimeException {
  public TokenGenerationException(String errorMessage) {
    super("Error while generating token for :" + errorMessage);
  }
}
