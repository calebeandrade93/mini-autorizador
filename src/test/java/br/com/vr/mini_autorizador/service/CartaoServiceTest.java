package br.com.vr.mini_autorizador.service;

import br.com.vr.mini_autorizador.dto.CartaoDTO;
import br.com.vr.mini_autorizador.entity.Cartao;
import br.com.vr.mini_autorizador.exceptions.CartaoExistenteException;
import br.com.vr.mini_autorizador.exceptions.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.exceptions.SenhaDoCartaoInvalidaException;
import br.com.vr.mini_autorizador.repository.CartaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {

    @InjectMocks
    private CartaoService service;

    @Mock
    private CartaoRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void novoCartao_deveCriarQuandoNaoExistente() {
        CartaoDTO dto = new CartaoDTO("123456789", "senha123");

        when(repository.findById(dto.numeroCartao())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.senha())).thenReturn("encodedSenha");

        CartaoDTO result = service.novoCartao(dto);

        assertEquals(dto, result);
        verify(repository).save(any(Cartao.class));
        verify(passwordEncoder).encode("senha123");
    }

    @Test
    void novoCartao_deveLancarExcecaoQuandoJaExistente() {
        CartaoDTO dto = new CartaoDTO("123456789", "senha123");
        Cartao existente = new Cartao();
        existente.setNumeroCartao(dto.numeroCartao());

        when(repository.findById(dto.numeroCartao())).thenReturn(Optional.of(existente));

        assertThrows(CartaoExistenteException.class, () -> service.novoCartao(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void consultaSaldo_deveRetornarSaldoQuandoCartaoExiste() {
        String numero = "123456789";
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao(numero);
        cartao.setSaldo(new BigDecimal("250.00"));

        when(repository.findById(numero)).thenReturn(Optional.of(cartao));

        BigDecimal saldo = service.consultaSaldo(numero);

        assertEquals(new BigDecimal("250.00"), saldo);
    }

    @Test
    void consultaSaldo_deveLancarExcecaoQuandoCartaoNaoExiste() {
        String numero = "999";
        when(repository.findById(numero)).thenReturn(Optional.empty());

        assertThrows(CartaoNaoEncontradoException.class, () -> service.consultaSaldo(numero));
    }

    @Test
    void validarSenha_devePassarQuandoSenhaCorreta() {
        Cartao cartao = new Cartao();
        cartao.setSenha("encodedSenha");

        when(passwordEncoder.matches("senha123", "encodedSenha")).thenReturn(true);

        assertDoesNotThrow(() -> service.validarSenha(cartao, "senha123"));
    }

    @Test
    void validarSenha_deveLancarExcecaoQuandoSenhaInvalida() {
        Cartao cartao = new Cartao();
        cartao.setSenha("encodedSenha");

        when(passwordEncoder.matches("senhaErrada", "encodedSenha")).thenReturn(false);

        assertThrows(SenhaDoCartaoInvalidaException.class,
                () -> service.validarSenha(cartao, "senhaErrada"));
    }
}
