package com.uex.api.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosRecuperarSenha(
        @NotBlank
        String email
) {
}
