package com.uex.api.controller;

import com.uex.api.domain.endereco.BuscarCep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    @Autowired
    private BuscarCep busca;

    @GetMapping("/{cep}")
    public ResponseEntity buscarCep(@PathVariable String cep) throws Exception {
        cep = cep.replaceAll("[^\\d ]", "");

        var enderecoCep = busca.buscaCep(cep);

        return ResponseEntity.ok(enderecoCep);
    }
}
