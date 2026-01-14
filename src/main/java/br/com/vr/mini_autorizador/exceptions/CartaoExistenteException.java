package br.com.vr.mini_autorizador.exceptions;

import br.com.vr.mini_autorizador.dto.CartaoDTO;
import lombok.Getter;

@Getter
public class CartaoExistenteException extends RuntimeException {

    private CartaoDTO cartaoDTO;

    public CartaoExistenteException(CartaoDTO cartaoDTO) {
         this.cartaoDTO = cartaoDTO;
    }
}