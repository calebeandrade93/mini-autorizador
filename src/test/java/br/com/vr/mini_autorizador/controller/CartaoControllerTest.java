package br.com.vr.mini_autorizador.controller;

import br.com.vr.mini_autorizador.dto.CartaoDTO;
import br.com.vr.mini_autorizador.exceptions.CartaoExistenteException;
import br.com.vr.mini_autorizador.exceptions.CartaoNaoEncontradoException;
import br.com.vr.mini_autorizador.service.CartaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaoControllerTest {

    @InjectMocks
    private CartaoController controller;

    @Mock
    private CartaoService cartaoService;

    @Test
    void criarCartao_deveRetornarCreated() {
        CartaoDTO request = new CartaoDTO("1234567891234567", "senha123");
        when(cartaoService.novoCartao(any(CartaoDTO.class))).thenReturn(request);

        ResponseEntity<CartaoDTO> response = controller.criarCartao(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(request, response.getBody());
        verify(cartaoService, times(1)).novoCartao(request);
    }

    @Test
    void criarCartao_deveRetornarUnprocessableEntity_quandoCartaoExistente() {
        CartaoDTO request = new CartaoDTO("1234567891234567", "senha123");
        when(cartaoService.novoCartao(any(CartaoDTO.class)))
                .thenThrow(new CartaoExistenteException(request));

        CartaoExistenteException exception = assertThrows(CartaoExistenteException.class, () -> {
            controller.criarCartao(request);
        });

        assertEquals(request, exception.getCartaoDTO());
        verify(cartaoService, times(1)).novoCartao(request);
    }

    @Test
    void consultaSaldo_deveRetornar200EValor() {
        String numeroCartao = "1234567891234567";
        BigDecimal saldo = new BigDecimal("100.50");
        when(cartaoService.consultaSaldo(numeroCartao)).thenReturn(saldo);

        ResponseEntity<BigDecimal> response = controller.consultaSaldo(numeroCartao);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(saldo, response.getBody());
        verify(cartaoService, times(1)).consultaSaldo(numeroCartao);
    }

    @Test
    void consultaSaldo_deveRetornar404_quandoCartaoInexistente() {
        String numeroCartao = "1234567891234567";
        when(cartaoService.consultaSaldo(numeroCartao))
                .thenThrow(new CartaoNaoEncontradoException("CARTAO_INEXISTENTE"));

        CartaoNaoEncontradoException exception = assertThrows(CartaoNaoEncontradoException.class, () -> {
            controller.consultaSaldo(numeroCartao);
        });

        assertEquals("CARTAO_INEXISTENTE", exception.getMessage());
        verify(cartaoService, times(1)).consultaSaldo(numeroCartao);
    }
}
