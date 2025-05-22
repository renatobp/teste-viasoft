package br.com.rbp.teste_viasoft.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Classe que representa o e-mail utilizando AWS.
 *
 * @author Renato Botelho Pereira
 */
@Getter
@Builder
@AllArgsConstructor
public class EmailAwsDTO {

    @NotBlank(message = "O e-mail do destinatário é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 45, message = "E-mail do destinatário deve ter no máximo 45 caracteres")
    private final String recipient;

    @NotBlank(message = "O nome do destinatário é obrigatório")
    @Size(max = 60, message = "Nome do destinatário deve ter no máximo 60 caracteres")
    private final String recipientName;

    @NotBlank(message = "O e-mail do remetente é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 45, message = "E-mail do remetente deve ter no máximo 45 caracteres")
    private final String sender;

    @NotBlank(message = "O assunto é obrigatório")
    @Size(max = 120, message = "Assunto deve ter no máximo 120 caracteres")
    private final String subject;

    @NotBlank(message = "O conteúdo é obrigatório")
    @Size(max = 256, message = "Conteúdo deve ter no máximo 256 caracteres")
    private final String content;
}