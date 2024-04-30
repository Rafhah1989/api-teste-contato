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

        Usuario user = (Usuario) usuarioRepository.findByEmail(usuario.email());

        if(user !=null) {
            throw new ValidacaoException("Email já cadastrado");
        }

    }
}
