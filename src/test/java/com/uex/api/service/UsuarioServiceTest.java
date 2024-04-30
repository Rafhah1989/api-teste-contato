package com.uex.api.service;

import com.uex.api.domain.ValidacaoException;
import com.uex.api.domain.contato.CadastroDeContatos;
import com.uex.api.domain.contato.ContatoRepository;
import com.uex.api.domain.contato.validacoes.cadastro.ValidadorCadastroDeContato;
import com.uex.api.domain.usuario.*;
import com.uex.api.domain.usuario.validacoes.UsuarioValidator;
import com.uex.api.domain.usuario.validacoes.ValidadorCadastroDeUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioValidator usuarioValidator;

    @Mock
    private List<ValidadorCadastroDeUsuario> validators;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve incluir um novo usuário com sucesso")
    void incluirUsuarioCenario1() {

        DadosCadastroUsuario dadosCadastroUsuario = new DadosCadastroUsuario("Joao", "joao@teste.com", "123456");

        Usuario usuario = new Usuario(null, "Joao", "joao@teste.com", "123456");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        var response = usuarioService.incluirUsuario(dadosCadastroUsuario);

        assertEquals(usuario, response);

    }

    @Test
    @DisplayName("Deve verificar se o email do usuário já existe")
    void incluirUsuarioCenario2() {

        DadosCadastroUsuario dadosCadastroUsuario = new DadosCadastroUsuario("Joao", "teste@teste.com", "123456");

        Usuario usuario = new Usuario(null, "Joao", "teste@teste.com", "123456");

        when(usuarioRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(ValidacaoException.class, () -> {
            usuarioValidator.validator(dadosCadastroUsuario);
        });
    }

    @Test
    @DisplayName("Deve excluir um novo usuário com sucesso")
    void excluirUsuarioCenario1() {

        DadosAutenticacao dadosAutenticacao = new DadosAutenticacao("teste@teste.com", "123456");

        Usuario usuario = new Usuario(1L, "Joao", "teste@teste.com", "$2a$10$rGCIr7B9yxQGxPi1tRCaDeYbo.gxMbDcVZY3ohLuHL3JR2kaiKmPe");

        when(usuarioRepository.findByEmail(any())).thenReturn(usuario);

        usuarioService.excluirUsuario(dadosAutenticacao);
    }

    @Test
    @DisplayName("Deve impedir a exclusão se a senha estiver incorreta e lançar uma exceção")
    void excluirUsuarioCenario2() {

        DadosAutenticacao dadosAutenticacao = new DadosAutenticacao("teste@teste.com", "654321");

        Usuario usuario = new Usuario(1L, "Joao", "teste@teste.com", "$2a$10$rGCIr7B9yxQGxPi1tRCaDeYbo.gxMbDcVZY3ohLuHL3JR2kaiKmPe");

        when(usuarioRepository.findByEmail(any())).thenReturn(usuario);

        assertThrows(ValidacaoException.class, () -> {
            usuarioService.excluirUsuario(dadosAutenticacao);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção se o email não for encontrado")
    void recuperarSenhaCenario1() {

        DadosRecuperarSenha dadosRecuperarSenha = new DadosRecuperarSenha("teste@teste.com");

        when(usuarioRepository.findByEmail(any())).thenReturn(null);

        assertThrows(ValidacaoException.class, () -> {
            usuarioService.recuperarSenha(dadosRecuperarSenha);
        });
    }
}
