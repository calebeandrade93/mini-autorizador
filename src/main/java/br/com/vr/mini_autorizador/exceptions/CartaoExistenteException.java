package br.com.vr.mini_autorizador.exceptions;

import lombok.Getter;

@Getter
public class CartaoExistenteException extends RuntimeException {

    private final String message;

    public CartaoExistenteException(String message) {
         this.message = message;
    }
}