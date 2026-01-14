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

    @Transactional
    public void processarTransacao(TransacaoDTO transacaoDTO){
        Cartao cartao = validarDadosCartao(transacaoDTO);
        if(cartao.getSaldo().compareTo(transacaoDTO.valor()) < 0){
            throw new SaldoInsuficienteException("SALDO_INSUFICIENTE");
        }

        cartao.setSaldo(cartao.getSaldo().subtract(transacaoDTO.valor()));
        cartaoRepository.save(cartao);

    }

    private Cartao validarDadosCartao(TransacaoDTO transacaoDTO){
        Cartao cartao = cartaoRepository.findById(transacaoDTO.numeroCartao()).orElseThrow(() -> new CartaoNaoEncontradoException("CARTAO_INEXISTENTE"));
        if (!passwordEncoder.matches(transacaoDTO.senhaCartao(), cartao.getSenha())) {
            throw new SenhaDoCartaoInvalidaException("SENHA_INVALIDA");
        }
        return cartao;
    }

}
