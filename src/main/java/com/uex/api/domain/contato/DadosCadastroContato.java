package com.uex.api.domain.contato;

import com.uex.api.domain.endereco.DadosEndereco;
import com.uex.api.domain.endereco.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroContato(
        @NotNull
        Long idUsuario,
        @NotNull
        String nome,
        @NotNull
        String cpf,
        @NotNull
        String telefone,
        @NotNull
        @Valid
        DadosEndereco endereco
) {
}
