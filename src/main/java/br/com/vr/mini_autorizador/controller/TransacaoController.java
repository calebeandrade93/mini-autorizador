package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.TransacaoDTO;
import br.com.vr.mini_autorizador.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<String> realizarTransacao(@RequestBody @Valid TransacaoDTO transacaoDTO) {
        transacaoService.processarTransacao(transacaoDTO);
        return ResponseEntity.status(201).body("OK");
    }

}
