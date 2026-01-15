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
    void processarTransacao_sucessoAoDebitar() {
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao("1234567890123456");
        cartao.setSenha("senha123");
        cartao.setSaldo(new BigDecimal("100.00"));

        TransacaoDTO dto = new TransacaoDTO("1234567890123456", "senha123", new BigDecimal("40.00"));

        when(repository.findByIdLock(dto.numeroCartao())).thenReturn(Optional.of(cartao));
        doNothing().when(cartaoService).validarSenha(cartao, "senha123");

        service.processarTransacao(dto);

        assertEquals(new BigDecimal("60.00"), cartao.getSaldo());
        verify(repository, times(1)).save(cartao);
        verify(repository, times(1)).findByIdLock(cartao.getNumeroCartao());
    }

    @Test
    void processarTransacao_deveLancarExcecaoQuandoCartaoNaoExiste() {
        TransacaoDTO dto = new TransacaoDTO("999", "senha", new BigDecimal("10.00"));

        when(repository.findByIdLock("999")).thenReturn(Optional.empty());

        CartaoNaoEncontradoException exception = assertThrows(CartaoNaoEncontradoException.class, () ->
                service.processarTransacao(dto));

        verify(repository, never()).save(any());
        verify(repository, times(1)).findByIdLock("999");
        assertEquals("CARTAO_INEXISTENTE", exception.getMessage());
    }

    @Test
    void processarTransacao_deveLancarExcecaoQuandoSaldoInsuficiente() {
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao("1234567890123456");
        cartao.setSenha("senha123");
        cartao.setSaldo(new BigDecimal("50.00"));

        TransacaoDTO dto = new TransacaoDTO("1234567890123456", "senha123", new BigDecimal("100.00"));

        when(repository.findByIdLock(dto.numeroCartao())).thenReturn(Optional.of(cartao));
        doNothing().when(cartaoService).validarSenha(cartao, "senha123");

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> service.processarTransacao(dto));
        verify(repository, times(1)).findByIdLock(dto.numeroCartao());
        verify(repository, never()).save(any());
        assertEquals("SALDO_INSUFICIENTE", exception.getMessage());
    }
}
