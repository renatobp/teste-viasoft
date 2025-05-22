package br.com.rbp.teste_viasoft.controllers;

import br.com.rbp.teste_viasoft.models.EmailDTO;
import br.com.rbp.teste_viasoft.services.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe controladora que gerencia as requisições do e-mail.
 *
 * @author Renato Botelho Pereira
 */
@RestController
@Validated
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Metodo responsável por receber as requisições de envio de e-mail.
     *
     * @param emailDTO
     * @author Renato Botelho Pereira
     */
    @PostMapping("/enviar")
    public ResponseEntity<String> enviarEmail(@Valid @RequestBody EmailDTO emailDTO) {
        return emailService.enviarEmail(emailDTO);
    }
}
