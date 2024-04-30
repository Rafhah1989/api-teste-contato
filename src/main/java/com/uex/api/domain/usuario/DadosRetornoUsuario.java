package com.uex.api.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosRetornoUsuario(
        Long id,
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email) {

        public DadosRetornoUsuario(Usuario usuario) {
                this(usuario.getId(), usuario.getNome(), usuario.getEmail());
        }
}
