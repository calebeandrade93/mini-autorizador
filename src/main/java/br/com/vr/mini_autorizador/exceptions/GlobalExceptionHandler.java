package br.com.vr.mini_autorizador.exceptions;

import br.com.vr.mini_autorizador.dto.CartaoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CartaoExistenteException.class)
    public ResponseEntity<CartaoDTO> handleCartaoExistente(CartaoExistenteException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getCartaoDTO());
    }

    @ExceptionHandler(CartaoNaoEncontrado.class)
    public ResponseEntity<String> handleCartaoNaoEncontrado(CartaoNaoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}