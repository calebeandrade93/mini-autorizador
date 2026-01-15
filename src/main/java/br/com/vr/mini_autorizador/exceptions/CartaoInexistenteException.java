package br.com.vr.mini_autorizador.exceptions;

public class CartaoInexistenteException extends RuntimeException {
    public CartaoInexistenteException(String message) {
        super(message);
    }

}