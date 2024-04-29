package com.uex.api.domain.usuario;

import com.uex.api.domain.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UsuarioValidator{

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void validator(DadosCadastroUsuario usuario) {

        Usuario user = (Usuario) usuarioRepository.findByEmail(usuario.email());

        if(user !=null) {
            throw new ValidacaoException("Email já cadastrado");
        }

    }
}
