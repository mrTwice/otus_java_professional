package ru.otus.java.professional.yampolskiy.spring.data.jdbc.exceptions;

public class ServiceException extends RuntimeException {
  private final String entityType;

  public ServiceException(String entityType, String message) {
    super(message);
    this.entityType = entityType;
  }

  public ServiceException(String entityType, String message, Throwable cause) {
    super(message, cause);
    this.entityType = entityType;
  }

  public String getEntityType() {
    return entityType;
  }

  @Override
  public String toString() {
    return String.format("ServiceException{entityType='%s', message='%s'}", entityType, getMessage());
  }
}
