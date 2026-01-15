package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.CartaoDTO;
import br.com.vr.mini_autorizador.entity.Cartao;
import br.com.vr.mini_autorizador.exceptions.CartaoExistenteException;
import br.com.vr.mini_autorizador.exceptions.CartaoInexistenteException;
import br.com.vr.mini_autorizador.exceptions.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.exceptions.SenhaDoCartaoInvalidaException;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public CartaoDTO novoCartao(CartaoDTO cartaoDTO) {
        return repository.findById(cartaoDTO.numeroCartao()).<CartaoDTO>map(cartaoExistente -> {
            throw new CartaoExistenteException(cartaoDTO);
        }).orElseGet(() -> {
            Cartao novoCartao = new Cartao();
            novoCartao.setNumeroCartao(cartaoDTO.numeroCartao());
            novoCartao.setSenha(passwordEncoder.encode(cartaoDTO.senha()));
            repository.save(novoCartao);
            return cartaoDTO;
        });
    }

    public BigDecimal consultaSaldo(String numeroCartao){
        Cartao cartao = repository.findById(numeroCartao).orElseThrow(() -> new CartaoNaoEncontradoException());
        return cartao.getSaldo();
    }

    public void validarSenha(Cartao cartao, String senhaInformada){
        if (!passwordEncoder.matches(senhaInformada, cartao.getSenha())) {
            throw new SenhaDoCartaoInvalidaException("SENHA_INVALIDA");
        }
    }
}