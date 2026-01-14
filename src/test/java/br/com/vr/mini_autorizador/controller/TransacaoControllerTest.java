package br.com.vr.mini_autorizador.controller;
import br.com.vr.mini_autorizador.dto.TransacaoDTO;
import br.com.vr.mini_autorizador.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoControllerTest {

    @InjectMocks
    private TransacaoController controller;

    @Mock
    private TransacaoService transacaoService;

    @Test
    void realizarTransacao_deveRetornarStatus201EStringOk() {
        TransacaoDTO transacaoDTO = new TransacaoDTO("1234567891234567", "senha1234", new BigDecimal("50.00"));

        doNothing().when(transacaoService).processarTransacao(any(TransacaoDTO.class));

        ResponseEntity<String> response = controller.realizarTransacao(transacaoDTO);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("OK", response.getBody());

        verify(transacaoService, times(1)).processarTransacao(transacaoDTO);
    }
}