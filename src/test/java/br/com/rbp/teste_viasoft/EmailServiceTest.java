package br.com.rbp.teste_viasoft;

import br.com.rbp.teste_viasoft.dtos.EmailAwsDTO;
import br.com.rbp.teste_viasoft.dtos.EmailOciDTO;
import br.com.rbp.teste_viasoft.handlers.EmailDTOValidator;
import br.com.rbp.teste_viasoft.dtos.EmailDTO;
import br.com.rbp.teste_viasoft.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailDTOValidator emailValidator;

    @InjectMocks
    private EmailService emailService;

    private EmailDTO emailDTO;

    @BeforeEach
    void setUp() {
        emailDTO = new EmailDTO(
                "destinatario@teste.com",
                "Destinatário Teste",
                "remetente@teste.com",
                "Teste de E-mail",
                "Conteúdo do e-mail de teste"
        );
    }

    @Test
    void deveEnviarEmailViaOCI() {
        ReflectionTestUtils.setField(emailService, "mailIntegracao", "OCI");
        when(emailValidator.validar(any(EmailOciDTO.class))).thenReturn(new ArrayList<>());

        ResponseEntity<String> response = emailService.enviarEmail(emailDTO);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void deveEnviarEmailViaAWS() {
        ReflectionTestUtils.setField(emailService, "mailIntegracao", "AWS");
        when(emailValidator.validar(any(EmailAwsDTO.class))).thenReturn(new ArrayList<>());

        ResponseEntity<String> response = emailService.enviarEmail(emailDTO);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void deveRetornarBadRequest_QuandoHouverErrosDeValidacao() {
        ReflectionTestUtils.setField(emailService, "mailIntegracao", "OCI");
        List<String> erros = List.of("Email inválido", "Nome obrigatório");
        when(emailValidator.validar(any())).thenReturn(erros);

        ResponseEntity<String> response = emailService.enviarEmail(emailDTO);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("ERROS DE VALIDAÇÃO"));
        assertTrue(response.getBody().contains("Email inválido"));
    }

    @Test
    void deveRetornarInternalServerError_QuandoOcorrerErroInesperado() {
        ReflectionTestUtils.setField(emailService, "mailIntegracao", "OCI");
        when(emailValidator.validar(any())).thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<String> response = emailService.enviarEmail(emailDTO);

        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("ERRO NO ENVIO DE E-MAIL VIA OCI"));
        assertTrue(response.getBody().contains("Erro inesperado"));
    }

    @Test
    void deveValidarCamposObrigatoriosDoEmailDTO() {
        EmailDTO emailInvalido = emailDTO;
        ReflectionTestUtils.setField(emailService, "mailIntegracao", "OCI");
        List<String> erros = List.of("Email do destinatário é obrigatório");
        when(emailValidator.validar(any())).thenReturn(erros);

        ResponseEntity<String> response = emailService.enviarEmail(emailInvalido);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("ERROS DE VALIDAÇÃO"));
    }
}