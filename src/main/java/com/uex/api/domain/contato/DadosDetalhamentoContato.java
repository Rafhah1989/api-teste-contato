package com.uex.api.domain.contato;

public record DadosDetalhamentoContato(Long id, Long idUsuario, String cpf, Double latitude, Double longitude) {

    public DadosDetalhamentoContato(Contato contato) {
        this(contato.getId(), contato.getUsuario().getId(), contato.getCpf(), contato.getEndereco().getLatitude(), contato.getEndereco().getLongitude());
    }
}
