package com.uex.api.domain.usuario.validacoes;

import com.uex.api.domain.usuario.DadosCadastroUsuario;

public interface ValidadorCadastroDeUsuario {

    void validator(DadosCadastroUsuario usuario);
}
