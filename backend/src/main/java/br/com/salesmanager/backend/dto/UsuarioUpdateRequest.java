package br.com.salesmanager.backend.dto;

import br.com.salesmanager.backend.model.Papel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotBlank(message = "email é obrigatório")
        @Email(message = "email inválido")
        String email,

        @Size(min = 6, message = "senha deve ter pelo menos 6 caracteres")
        String senha,

        @NotNull(message = "papel é obrigatório")
        Papel papel
) {
}