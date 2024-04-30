package com.uex.api.domain.usuario.validacoes;

import com.uex.api.domain.ValidacaoException;
import com.uex.api.domain.usuario.DadosCadastroUsuario;
import com.uex.api.domain.usuario.Usuario;
import com.uex.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidator implements ValidadorCadastroDeUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void validator(DadosCadastroUsuario usuario) {

        boolean existeUsuario = usuarioRepository.existsByEmail(usuario.email());

        if(existeUsuario) {
            throw new ValidacaoException("Email já cadastrado");
        }

    }
}
