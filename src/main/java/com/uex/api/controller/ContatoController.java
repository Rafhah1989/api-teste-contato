package com.uex.api.controller;

import com.uex.api.domain.contato.CadastroDeContatos;
import com.uex.api.domain.contato.ContatoRepository;
import com.uex.api.domain.contato.DadosCadastroContato;
import com.uex.api.domain.contato.DadosListagemContato;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private CadastroDeContatos cadastroDeContatos;

    @Autowired
    private ContatoRepository contatoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar (@RequestBody @Valid DadosCadastroContato dados, UriComponentsBuilder uriBuilder){
        var dto = cadastroDeContatos.cadastrar(dados);

        var uri = uriBuilder.path("/contato/{id}").buildAndExpand(dto.id()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemContato>> listar(@PageableDefault(size = 10, sort = {"nome"},
            direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = contatoRepository.findAllWithUsuario(paginacao).map(DadosListagemContato::new);

        return ResponseEntity.ok(page);
    }
}
