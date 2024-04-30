package com.uex.api.domain.contato;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.uex.api.domain.ValidacaoException;
import com.uex.api.domain.contato.validacoes.cadastro.ValidadorCadastroDeContato;
import com.uex.api.domain.endereco.Endereco;
import com.uex.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CadastroDeContatos {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private List<ValidadorCadastroDeContato> validadores;

    public DadosDetalhamentoContato cadastrar(DadosCadastroContato dados){

        if (contatoRepository.existsByCpfAndUsuarioId(dados.cpf(), dados.idUsuario())){
            throw new ValidacaoException("CPF já cadastrado!");
        }

        var usuario = usuarioRepository.findById(dados.idUsuario());

        if (!usuario.isPresent()){
            throw new ValidacaoException("Usuário não encontrado!");
        }

        validadores.forEach(v -> v.validar(dados));

        var contato = new Contato(null, usuario.get(), dados.nome(), dados.cpf(), dados.telefone(), new Endereco(dados.endereco()));

        consultarLatitudeLongitude(contato);

        contatoRepository.save(contato);

        return new DadosDetalhamentoContato(contato);
    }

    private void consultarLatitudeLongitude(Contato contato) {

        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyAamYxWYgIsKTjtHCWIgghoHyDGbRr1MBc")
                    .build();

            GeocodingResult[] results = new GeocodingResult[0];

            results = GeocodingApi.geocode(context,
                    "Brasil, CEP " + contato.getEndereco().getCep() + ", Número " + contato.getEndereco().getNumero())
                    .await();


            contato.getEndereco().setLatitude(results[0].geometry.location.lat);
            contato.getEndereco().setLongitude(results[0].geometry.location.lng);

        } catch (ApiException e) {
            throw new ValidacaoException("Não foi possível localizar as coordenadas do endereço informado!");
        } catch (InterruptedException e) {
            throw new ValidacaoException("Não foi possível localizar as coordenadas do endereço informado!");
        } catch (IOException e) {
            throw new ValidacaoException("Não foi possível localizar as coordenadas do endereço informado!");
        }
    }
}
