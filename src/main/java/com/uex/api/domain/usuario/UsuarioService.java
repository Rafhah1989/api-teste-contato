package com.uex.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private List<UsuarioValidator> validators;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Usuario incluirUsuario(DadosCadastroUsuario dadosCadastroUsuario) {

        validators.forEach(v -> v.validator(dadosCadastroUsuario));

        Usuario usuario = new Usuario();

        String encriptedPasswd = bCryptPasswordEncoder.encode(dadosCadastroUsuario.senha());

        usuario.setEmail(dadosCadastroUsuario.email());
        usuario.setSenha(encriptedPasswd);
        usuario.setNome(dadosCadastroUsuario.nome());

        userRepository.save(usuario);

        return usuario;

    }
}
