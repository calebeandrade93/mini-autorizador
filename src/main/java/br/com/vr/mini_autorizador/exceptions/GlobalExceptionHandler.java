package br.com.vr.mini_autorizador.exceptions;

import br.com.vr.mini_autorizador.dto.CartaoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(404).body(errorMessage);
    }

    @ExceptionHandler(CartaoExistenteException.class)
    public ResponseEntity<CartaoDTO> handleCartaoExistente(CartaoExistenteException ex) {
        return ResponseEntity.status(422).body(ex.getCartaoDTO());
    }

    @ExceptionHandler(CartaoNaoEncontradoException.class)
    public ResponseEntity<String> handleCartaoNaoEncontrado(CartaoNaoEncontradoException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(SenhaDoCartaoInvalidaException.class)
    public ResponseEntity<String> handleSenhaDoCartaoInvalida(SenhaDoCartaoInvalidaException ex) {
        return ResponseEntity.status(422).body(ex.getMessage());
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<String> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
        return ResponseEntity.status(422).body(ex.getMessage());
    }
}