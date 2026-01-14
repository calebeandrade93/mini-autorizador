package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.CartaoDTO;
import br.com.vr.mini_autorizador.exceptions.CartaoExistenteException;
import br.com.vr.mini_autorizador.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoDTO> criarCartao(@RequestBody @Valid CartaoDTO cartaoDTO) {
        CartaoDTO novoCartao = cartaoService.novoCartao(cartaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartaoDTO);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> consultaSaldo(@PathVariable String numeroCartao){
        BigDecimal saldo = cartaoService.consultaSaldo(numeroCartao);
        return ResponseEntity.status(HttpStatus.OK).body(saldo);
    }

}
