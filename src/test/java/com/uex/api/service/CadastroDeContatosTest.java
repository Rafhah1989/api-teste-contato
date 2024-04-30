package com.uex.api.service;

import com.uex.api.domain.ValidacaoException;
import com.uex.api.domain.contato.*;
import com.uex.api.domain.contato.validacoes.cadastro.ValidadorCadastroDeContato;
import com.uex.api.domain.endereco.DadosEndereco;
import com.uex.api.domain.endereco.Endereco;
import com.uex.api.domain.usuario.Usuario;
import com.uex.api.domain.usuario.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CadastroDeContatosTest {

    @InjectMocks
    private CadastroDeContatos cadastroDeContatos;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private List<ValidadorCadastroDeContato> validadores;


    @Test
    @DisplayName("Deve cadastrar um contato com sucesso")
    void cadastrarContatoCenario1() {

        var dadosEndereco =  new DadosEndereco("Test Address", "123", "Casa", "Bairro", "Lages", "SC", "88509-000",
                -1.0, 5.0);

        DadosCadastroContato dadosCadastroContato = new DadosCadastroContato(1L, "Test Contact", "12345678901", "1234567890",
                dadosEndereco);

        Usuario usuario = new Usuario(1L, "Test User", "test@test.com", "password");
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        Contato contato = new Contato(null, usuario, "Test Contact", "12345678901", "1234567890", new Endereco(dadosEndereco));
        when(contatoRepository.save(any(Contato.class))).thenAnswer(i -> i.getArguments()[0]);

        DadosDetalhamentoContato result = cadastroDeContatos.cadastrar(dadosCadastroContato);

        assertEquals(1L, result.idUsuario());
        assertEquals("12345678901", result.cpf());

    }

    @Test
    @DisplayName("Deve retornar exceção ao tentar cadastrar com um usuário inexistente")
    void cadastrarContatoCenario2() {

        var dadosEndereco =  new DadosEndereco("Test Address", "123", "Casa", "Bairro", "Lages", "SC", "88509-000",
                -1.0, 5.0);

        DadosCadastroContato dadosCadastroContato = new DadosCadastroContato(1L, "Test Contact", "12345678901", "1234567890",
                dadosEndereco);

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ValidacaoException.class, () -> {
            cadastroDeContatos.cadastrar(dadosCadastroContato);
        });

    }

    @Test
    @DisplayName("Deve retornar exceção ao tentar cadastrar com um uusuario com cpf já cadastrado")
    void cadastrarContatoCenario3() {

        var dadosEndereco =  new DadosEndereco("Test Address", "123", "Casa", "Bairro", "Lages", "SC", "88509-000",
                -1.0, 5.0);

        DadosCadastroContato dadosCadastroContato = new DadosCadastroContato(1L, "Test Contact", "12345678901", "1234567890",
                dadosEndereco);

        when(contatoRepository.existsByCpfAndUsuarioId(any(), any())).thenReturn(true);

        assertThrows(ValidacaoException.class, () -> {
            cadastroDeContatos.cadastrar(dadosCadastroContato);
        });

    }

}
