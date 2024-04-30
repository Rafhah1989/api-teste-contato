package com.uex.api.controller;

import com.uex.api.domain.usuario.*;
import com.uex.api.infra.security.DadosTokenJWT;
import com.uex.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<?> cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dadosCadastroUsuario, UriComponentsBuilder uriBuilder) {

        Usuario usuario = usuarioService.incluirUsuario(dadosCadastroUsuario);

        var uriComponentsBuilder = uriBuilder.path("/login/{id}");

        UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(usuario.getId());

        return ResponseEntity.created(uriComponents.toUri()).body(new DadosUsuarioCadastrado(usuario.getId(), usuario.getNome(), usuario.getEmail()));

    }

    @PostMapping("/recuperar")
    @Transactional
    public ResponseEntity<?> recuperarSenha(@RequestBody @Valid DadosRecuperarSenha dadosRecuperarSenha, UriComponentsBuilder uriBuilder) {

        Usuario usuario = usuarioService.recuperarSenha(dadosRecuperarSenha);

        return ResponseEntity.ok(new DadosUsuarioCadastrado(usuario.getId(), usuario.getNome(), usuario.getEmail()));

    }

    @DeleteMapping
    @Transactional
    public ResponseEntity excluir(@RequestBody @Valid DadosAutenticacao dados){

        usuarioService.excluirUsuario(dados);

        return ResponseEntity.noContent().build();
    }
}
