package br.com.rbp.teste_viasoft.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Classe que representa o e-mail utilizando OCI.
 *
 * @author Renato Botelho Pereira
 */
@Getter
@Builder
@AllArgsConstructor
public class EmailOciDTO {

    @NotBlank(message = "O e-mail do destinatário é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 40, message = "O e-mail do destinatário deve ter no máximo 40 caracteres")
    private final String recipientEmail;

    @NotBlank(message = "O nome do destinatário é obrigatório")
    @Size(max = 50, message = "O nome do destinatário deve ter no máximo 50 caracteres")
    private final String recipientName;

    @NotBlank(message = "O e-mail do remetente é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 40, message = "O e-mail do remetente deve ter no máximo 40 caracteres")
    private final String senderEmail;

    @NotBlank(message = "O assunto do e-mail é obrigatório")
    @Size(max = 100, message = "O assunto deve ter no máximo 100 caracteres")
    private final String subject;

    @NotBlank(message = "O conteúdo do e-mail é obrigatório")
    @Size(max = 250, message = "O conteúdo deve ter no máximo 250 caracteres")
    private final String body;
}
