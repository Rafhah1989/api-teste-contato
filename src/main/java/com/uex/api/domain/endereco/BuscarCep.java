package com.uex.api.domain.endereco;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class BuscarCep {

    public EnderecoCep buscaCep(String cep) throws Exception {
        String enderecoURL = "https://viacep.com.br/ws/" + cep + "/json/";
        URL url = new URL(enderecoURL);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.setDoInput(true);
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader((conexao.getInputStream()), "utf-8"));

            String convertJsonString = converteJsonEmString(buff);
            Gson gson = new Gson();
            EnderecoCep endereco = gson.fromJson(convertJsonString, EnderecoCep.class);
            return endereco;

        } catch (Exception msgErro) {
            throw new Exception("Erro de conexão- status Code [" + conexao.getResponseCode() + "]. " + msgErro.toString());
        }
    }

    private static String converteJsonEmString(BufferedReader buffereReader) throws IOException {
        String resposta, jsonString = "";
        while ((resposta = buffereReader.readLine()) != null) {
            jsonString += resposta;
        }
        return jsonString;
    }
}
