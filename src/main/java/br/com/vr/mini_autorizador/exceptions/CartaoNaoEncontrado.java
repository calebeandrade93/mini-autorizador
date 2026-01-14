package br.com.vr.mini_autorizador.exceptions;

public class CartaoNaoEncontrado extends RuntimeException {
    public CartaoNaoEncontrado(String message) {
        super(message);
    }
}
