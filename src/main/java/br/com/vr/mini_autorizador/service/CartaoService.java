package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.CartaoDTO;
import br.com.vr.mini_autorizador.entity.Cartao;
import br.com.vr.mini_autorizador.exceptions.CartaoExistenteException;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional //TODO: Verificar se é necessário
    public CartaoDTO novoCartao(CartaoDTO cartaoDTO) {
        return repository.findById(cartaoDTO.getNumeroCartao()).<CartaoDTO>map(cartaoExistente -> {
            throw new CartaoExistenteException("Cartão já existente");
        }).orElseGet(() -> {
            Cartao novoCartao = new Cartao();
            novoCartao.setNumeroCartao(cartaoDTO.getNumeroCartao());
            novoCartao.setSenha(passwordEncoder.encode(cartaoDTO.getSenha()));
            repository.save(novoCartao);
            return cartaoDTO;
        });
    }
}
