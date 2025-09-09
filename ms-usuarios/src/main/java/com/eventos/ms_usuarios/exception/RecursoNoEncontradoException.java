package com.eventos.ms_usuarios.exception;

public class RecursoNoEncontradoException extends RuntimeException {
  public RecursoNoEncontradoException(String recurso, Object id) {
    super(recurso + " con id " + id + " no existe");
  }
}
