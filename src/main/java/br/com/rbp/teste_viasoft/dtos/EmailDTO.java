package br.com.rbp.teste_viasoft.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailDTO {
    private final String emailDestinatario;
    private final String nomeDestinatario;
    private final String emailRemetente;
    private final String assunto;
    private final String conteudo;
}
