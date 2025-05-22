package br.com.rbp.teste_viasoft.services;

import br.com.rbp.teste_viasoft.dtos.EmailAwsDTO;
import br.com.rbp.teste_viasoft.dtos.EmailOciDTO;
import br.com.rbp.teste_viasoft.handlers.EmailDTOValidator;
import br.com.rbp.teste_viasoft.dtos.EmailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Classe de serviço para processar e enviar e-mails.
 *
 * @author Renato Botelho Pereira
 */
@Service
@Validated
public class EmailService {

    @Value("${mail.integracao}")
    private String mailIntegracao;

    @Autowired
    private EmailDTOValidator emailValidator;

    /**
     * Metodo responsável por converter os dados do e-mail para o formato OCI.
     *
     * @param emailDTO
     * @return EmailOciDTO
     * @author Renato Botelho Pereira
     */
    private EmailOciDTO converterParaOciDTO(@Valid EmailDTO emailDTO) {
        return EmailOciDTO.builder()
                .recipientEmail(emailDTO.getEmailDestinatario())
                .recipientName(emailDTO.getNomeDestinatario())
                .senderEmail(emailDTO.getEmailRemetente())
                .subject(emailDTO.getAssunto())
                .body(emailDTO.getConteudo())
                .build();
    }

    /**
     * Metodo responsável por converter os dados do e-mail para o formato AWS.
     *
     * @param emailDTO
     * @return EmailAwsDTO
     * @author Renato Botelho Pereira
     */
    private EmailAwsDTO converterParaAwsDTO(@Valid EmailDTO emailDTO) {
        return EmailAwsDTO.builder()
                .recipient(emailDTO.getEmailDestinatario())
                .recipientName(emailDTO.getNomeDestinatario())
                .sender(emailDTO.getEmailRemetente())
                .subject(emailDTO.getAssunto())
                .content(emailDTO.getConteudo())
                .build();
    }

    /**
     * Metodo responsável por enviar o e-mail se tudo ocorrer com sucesso ou retornar um ero caso haja uma falha na requisição.
     *
     * @param emailDTO
     * @return ResponseEntity<String>
     * @author Renato Botelho Pereira
     */
    public ResponseEntity<String> enviarEmail(@Valid EmailDTO emailDTO) {
        try {
            // Conversão e validação do email
            Object emailObject = mailIntegracao.equals("OCI") ?
                    converterParaOciDTO(emailDTO) :
                    converterParaAwsDTO(emailDTO);

            List<String> violacoes = emailValidator.validar(emailObject);
            if (!violacoes.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(construirMensagemErro("ERROS DE VALIDAÇÃO", violacoes));
            }

            // Serialização do objeto
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String emailJson = objectMapper.writeValueAsString(emailObject);

            // Registro do envio
            String mensagemSucesso = construirMensagemSucesso(emailJson);
            System.out.println(mensagemSucesso);
            return ResponseEntity.noContent().build();

        } catch (JsonProcessingException e) {
            String erro = "Erro ao serializar objeto de e-mail: " + e.getMessage();
            System.err.println(erro);
            return ResponseEntity.internalServerError().body(erro);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(construirMensagemErro(
                            String.format("ERRO NO ENVIO DE E-MAIL VIA %s", mailIntegracao),
                            List.of(e.getMessage())
                    ));
        }
    }


    /**
     * Metodo responsável por montar a mensagem caso haja sucesso na requisição.
     *
     * @param titulo
     * @param mensagens
     * @return String
     * @author Renato Botelho Pereira
     */
    private String construirMensagemErro(String titulo, List<String> mensagens) {
        StringBuilder msg = new StringBuilder();
        msg.append("==========================================\n");
        msg.append(titulo).append("\n");
        msg.append("==========================================\n");
        msg.append("Timestamp: ").append(java.time.LocalDateTime.now()).append("\n");
        msg.append("------------------------------------------\n");
        mensagens.forEach(erro -> msg.append("- ").append(erro).append("\n"));
        return msg.toString();
    }

    /**
     * Metodo responsável por montar a mensagem caso haja sucesso na requisição.
     *
     * @param emailJson
     * @return String
     * @author Renato Botelho Pereira
     */
    private String construirMensagemSucesso(String emailJson) {
        StringBuilder dadosEmail = new StringBuilder();
        dadosEmail.append("==========================================\n");
        dadosEmail.append(String.format("ENVIO DE E-MAIL VIA %s\n", mailIntegracao));
        dadosEmail.append("==========================================\n");
        dadosEmail.append(String.format("Provedor: %s\n", mailIntegracao));
        dadosEmail.append("Status: Processando envio\n\n");
        dadosEmail.append("DETALHES DO E-MAIL:\n");
        dadosEmail.append("------------------------------------------\n");
        dadosEmail.append(emailJson).append("\n");
        dadosEmail.append("------------------------------------------\n\n");
        dadosEmail.append(String.format("STATUS: E-mail enviado com sucesso via %s", mailIntegracao));
        return dadosEmail.toString();
    }

}