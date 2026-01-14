package br.com.vr.mini_autorizador.exceptions;

public class SenhaDoCartaoInvalidaException extends RuntimeException {
    public SenhaDoCartaoInvalidaException(String message) {
        super(message);
    }
}