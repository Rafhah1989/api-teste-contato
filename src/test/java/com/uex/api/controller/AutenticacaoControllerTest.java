package com.uex.api.controller;

import com.uex.api.domain.usuario.DadosCadastroUsuario;
import com.uex.api.domain.usuario.DadosRetornoUsuario;
import com.uex.api.domain.usuario.Usuario;
import com.uex.api.domain.usuario.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UsuarioRepository usuarioRepository;

    @Autowired
    private JacksonTester<DadosCadastroUsuario> dadosCadastroUsuarioJson;

    @Autowired
    private JacksonTester<DadosRetornoUsuario> dadosRetornoUsuarioJson;

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando o usuario for cadastrado com sucesso")
    @WithMockUser
    void cadastrarUsuario_cenario1() throws Exception {

        var dadosCadastroUsuario = new DadosCadastroUsuario("teste", "teste@teste.com", "123456");
        var usuario = new Usuario(null, "teste", "teste@teste.com", "123456");

        when(usuarioRepository.save(Mockito.any())).thenReturn(usuario);

        var response = mvc.perform(MockMvcRequestBuilders.post("/login/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroUsuarioJson.write(dadosCadastroUsuario).getJson()))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = dadosRetornoUsuarioJson.write(new DadosRetornoUsuario(usuario)).getJson();

        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes invalidas")
    @WithMockUser
    void cadastrarUsuario_cenario2() throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.post("/login/cadastrar"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando recuperada a senha")
    @WithMockUser
    void recuperarSenha_cenario1() throws Exception {

        var dadosCadastroUsuario = new DadosCadastroUsuario("teste", "teste@teste.com", "123456");
        var usuario = new Usuario(null, "teste", "teste@teste.com", "123456");

        when(usuarioRepository.findByEmail(any())).thenReturn(usuario);

        var response = mvc.perform(MockMvcRequestBuilders.post("/login/recuperar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroUsuarioJson.write(dadosCadastroUsuario).getJson()))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes invalidas")
    @WithMockUser
    void recuperarSenha_cenario2() throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.post("/login/recuperar"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
