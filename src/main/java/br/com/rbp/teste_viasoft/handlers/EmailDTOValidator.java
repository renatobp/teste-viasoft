package br.com.rbp.teste_viasoft.handlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * Classe para validar os dados na requisição.
 *
 * @author Renato Botelho Pereira
 */
@Component
public class EmailDTOValidator {

    private final Validator validator;

    public EmailDTOValidator() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Metodo para validar os campos mapeados com o bean validation.
     *
     * @param emailDTO
     * @return
     * @param <T>
     */
    public <T> List<String> validar(T emailDTO) {
        List<String> violacoes = new ArrayList<>();

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(emailDTO);
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<T> violation : constraintViolations) {
                violacoes.add(violation.getMessage());
            }
        }

        return violacoes;
    }

}