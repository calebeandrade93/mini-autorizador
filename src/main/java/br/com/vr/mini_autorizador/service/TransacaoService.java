package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.TransacaoDTO;
import br.com.vr.mini_autorizador.entity.Cartao;
import br.com.vr.mini_autorizador.exceptions.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.exceptions.SaldoInsuficienteException;
import br.com.vr.mini_autorizador.exceptions.SenhaDoCartaoInvalidaException;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransacaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartaoService cartaoService;

    @Transactional
    public void processarTransacao(TransacaoDTO transacaoDTO){

        Cartao cartao = cartaoRepository.findByIdLock(transacaoDTO.numeroCartao()).orElseThrow(() ->
                new CartaoNaoEncontradoException("CARTAO_INEXISTENTE"));
        cartaoService.validarSenha(cartao, transacaoDTO.senhaCartao());

        if(cartao.getSaldo().compareTo(transacaoDTO.valor()) < 0){
            throw new SaldoInsuficienteException("SALDO_INSUFICIENTE");
        }

        cartao.setSaldo(cartao.getSaldo().subtract(transacaoDTO.valor()));
        cartaoRepository.save(cartao);
    }
}