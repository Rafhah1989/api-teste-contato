package com.uex.api.domain.contato;

import com.uex.api.domain.endereco.Endereco;
import com.uex.api.domain.usuario.DadosUsuarioCadastrado;
import com.uex.api.domain.usuario.Usuario;

public record DadosListagemContato(Long id, DadosUsuarioCadastrado usuario, String nome, String cpf, String telefone, Endereco endereco) {

    public DadosListagemContato(Contato contato) {
        this(contato.getId(),
                new DadosUsuarioCadastrado(contato.getUsuario().getId(), contato.getUsuario().getNome(),
                        contato.getUsuario().getEmail()), contato.getNome(), contato.getCpf(), contato.getTelefone(),
                contato.getEndereco());
    }
}
