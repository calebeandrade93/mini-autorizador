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
    void novoCartao_sucessoAoCriar() {
        CartaoDTO dto = new CartaoDTO("1234567890123456", "senha123");

        when(repository.findById(dto.numeroCartao())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.senha())).thenReturn("senha123_encoded");

        CartaoDTO resultado = service.novoCartao(dto);

        assertEquals(dto, resultado);
        verify(repository, times(1)).findById("1234567890123456");
        verify(repository, times(1)).save(any(Cartao.class));
        verify(passwordEncoder).encode("senha123");
    }

    @Test
    void novoCartao_deveLancarExcecaoQuandoExistente() {
        CartaoDTO dto = new CartaoDTO("1234567890123456", "senha123");
        Cartao novoCartao = new Cartao();
        novoCartao.setNumeroCartao(dto.numeroCartao());

        when(repository.findById(dto.numeroCartao())).thenReturn(Optional.of(novoCartao));

        assertThrows(CartaoExistenteException.class, () -> service.novoCartao(dto));
        verify(repository, never()).save(any(Cartao.class));
        verify(repository, times(1)).findById(novoCartao.getNumeroCartao());
    }

    @Test
    void consultaSaldo_sucessoAoConsultar() {
        Cartao novoCartao = new Cartao();
        novoCartao.setNumeroCartao("1234567890123456");
        novoCartao.setSenha("senha123");
        novoCartao.setSaldo(new BigDecimal("250.00"));

        when(repository.findById(novoCartao.getNumeroCartao())).thenReturn(Optional.of(novoCartao));

        BigDecimal saldo = service.consultaSaldo(novoCartao.getNumeroCartao());

        assertEquals(new BigDecimal("250.00"), saldo);
        verify(repository, times(1)).findById(novoCartao.getNumeroCartao());
    }

    @Test
    void consultaSaldo_deveLancarExcecaoQuandoCartaoNaoExiste() {
        CartaoDTO dto = new CartaoDTO("0000000000000000", "senha123");
        when(repository.findById(dto.numeroCartao())).thenReturn(Optional.empty());

        CartaoNaoEncontradoException exception = assertThrows(CartaoNaoEncontradoException.class, () ->
                service.consultaSaldo(dto.numeroCartao()));

        assertEquals("CARTAO_INEXISTENTE", exception.getMessage());
        verify(repository, times(1)).findById(dto.numeroCartao());
    }

    @Test
    void validarSenha_sucessoAoValidar() {
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

        SenhaDoCartaoInvalidaException exception = assertThrows(SenhaDoCartaoInvalidaException.class, () ->
                service.validarSenha(cartao, "senhaErrada"));

        assertEquals("SENHA_INVALIDA", exception.getMessage());
    }
}
