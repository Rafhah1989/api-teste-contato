package com.uex.api.domain.usuario;

import com.uex.api.domain.ValidacaoException;
import com.uex.api.domain.contato.ContatoRepository;
import com.uex.api.domain.usuario.validacoes.ValidadorCadastroDeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private List<ValidadorCadastroDeUsuario> validators;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JavaMailSender envioEmailJava;

    public Usuario incluirUsuario(DadosCadastroUsuario dadosCadastroUsuario) {

        validators.forEach(v -> v.validator(dadosCadastroUsuario));

        Usuario usuario = new Usuario();

        String encriptedPasswd = bCryptPasswordEncoder.encode(dadosCadastroUsuario.senha());

        usuario.setEmail(dadosCadastroUsuario.email());
        usuario.setSenha(encriptedPasswd);
        usuario.setNome(dadosCadastroUsuario.nome());

        usuarioRepository.save(usuario);

        return usuario;

    }

    public void excluirUsuario(DadosAutenticacao dadosAutenticacao) {

        Usuario usuario = (Usuario) usuarioRepository.findByEmail(dadosAutenticacao.email());

        if (usuario == null){
            throw new ValidacaoException("Usuário não encontrado com o email informado!");
        }

        if (!BCrypt.checkpw(dadosAutenticacao.senha(), usuario.getSenha())) {
            throw new ValidacaoException("Senha inválida!");
        }

        contatoRepository.deleteByUsuarioId(usuario.getId());
        usuarioRepository.deleteById(usuario.getId());

    }

    public Usuario recuperarSenha(DadosRecuperarSenha dadosRecuperarSenha) {

        var usuario = (Usuario) usuarioRepository.findByEmail(dadosRecuperarSenha.email());

        if (usuario == null){
            throw new ValidacaoException("Email não encontrado!");
        }

        enviarEmail(usuario.getEmail(), "Recuperação de Senha", mensagemRecuperacaoSenha(usuario));

        return usuario;
    }

    private void enviarEmail(String para, String titulo, String conteudo){

        var mensagem = new SimpleMailMessage();

        mensagem.setTo(para);
        mensagem.setSubject(titulo);
        mensagem.setText(conteudo);

        envioEmailJava.send(mensagem);
    }

    private String mensagemRecuperacaoSenha(Usuario usuario){

        StringBuffer mensagem = new StringBuffer();

        mensagem.append("Olá ").append(usuario.getNome()).append("!\n\n");
        mensagem.append("Crie uma nova senha através do link: ").append("\n\n");
        mensagem.append("http://novaSenha.com").append("\n\n");

        return mensagem.toString();
    }
}
