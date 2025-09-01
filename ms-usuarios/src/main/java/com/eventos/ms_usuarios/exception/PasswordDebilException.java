package com.eventos.ms_usuarios.exception;

public class PasswordDebilException extends RuntimeException {
  public PasswordDebilException() {
    super("La contraseña es muy débil (mínimo 8 caracteres, letra y número)");
  }
}
