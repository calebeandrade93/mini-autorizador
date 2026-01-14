package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.TransacaoDTO;
import br.com.vr.mini_autorizador.entity.Cartao;
import br.com.vr.mini_autorizador.exceptions.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.exceptions.SaldoInsuficienteException;
import br.com.vr.mini_autorizador.exceptions.SenhaDoCartaoInvalidaException;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService service;

    @Mock
    private CartaoRepository repository;

    @Mock
    private CartaoService cartaoService;

    @Test
    void processarTransacao_deveDebitarSaldoQuandoSucesso() {
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao("123");
        cartao.setSaldo(new BigDecimal("100.00"));

        TransacaoDTO dto = new TransacaoDTO("123", "senha123", new BigDecimal("40.00"));

        when(repository.findByIdLock("123")).thenReturn(Optional.of(cartao));
        // senha válida
        doNothing().when(cartaoService).validarSenha(cartao, "senha123");

        service.processarTransacao(dto);

        assertEquals(new BigDecimal("60.00"), cartao.getSaldo());
        verify(repository).save(cartao);
    }

    @Test
    void processarTransacao_deveLancarExcecaoQuandoCartaoNaoExiste() {
        TransacaoDTO dto = new TransacaoDTO("999", "senha", new BigDecimal("10.00"));

        when(repository.findByIdLock("999")).thenReturn(Optional.empty());

        assertThrows(CartaoNaoEncontradoException.class, () -> service.processarTransacao(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void processarTransacao_deveLancarExcecaoQuandoSenhaInvalida() {
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao("123");
        cartao.setSaldo(new BigDecimal("100.00"));

        TransacaoDTO dto = new TransacaoDTO("123", "senhaErrada", new BigDecimal("10.00"));

        when(repository.findByIdLock("123")).thenReturn(Optional.of(cartao));
        doThrow(new SenhaDoCartaoInvalidaException("SENHA_INVALIDA"))
                .when(cartaoService).validarSenha(cartao, "senhaErrada");

        assertThrows(SenhaDoCartaoInvalidaException.class, () -> service.processarTransacao(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void processarTransacao_deveLancarExcecaoQuandoSaldoInsuficiente() {
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao("123");
        cartao.setSaldo(new BigDecimal("50.00"));

        TransacaoDTO dto = new TransacaoDTO("123", "senha123", new BigDecimal("100.00"));

        when(repository.findByIdLock("123")).thenReturn(Optional.of(cartao));
        doNothing().when(cartaoService).validarSenha(cartao, "senha123");

        assertThrows(SaldoInsuficienteException.class, () -> service.processarTransacao(dto));
        verify(repository, never()).save(any());
    }
}
