package br.com.salesmanager.desktop.http;

import br.com.salesmanager.desktop.session.SessaoUsuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiClient {

    public static final String SERVER_URL = "http://localhost:8080";
    private static final String BASE_URL = SERVER_URL + "/api";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T get(String caminho, Class<T> tipoResposta) {
        return converter(enviar(requestBuilder(caminho).GET().build()), tipoResposta);
    }

    public <T> T get(String caminho, TypeReference<T> tipoResposta) {
        return converter(enviar(requestBuilder(caminho).GET().build()), tipoResposta);
    }

    public <T> T post(String caminho, Object corpo, Class<T> tipoResposta) {
        HttpRequest request = requestBuilder(caminho)
                .POST(HttpRequest.BodyPublishers.ofString(paraJson(corpo)))
                .build();
        return converter(enviar(request), tipoResposta);
    }

    public <T> T put(String caminho, Object corpo, Class<T> tipoResposta) {
        HttpRequest request = requestBuilder(caminho)
                .PUT(HttpRequest.BodyPublishers.ofString(paraJson(corpo)))
                .build();
        return converter(enviar(request), tipoResposta);
    }

    public void delete(String caminho) {
        converter(enviar(requestBuilder(caminho).DELETE().build()), Void.class);
    }

    private String paraJson(Object corpo) {
        try {
            return objectMapper.writeValueAsString(corpo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao montar requisição: " + e.getMessage(), e);
        }
    }

    private HttpRequest.Builder requestBuilder(String caminho) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + caminho))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10));

        String token = SessaoUsuario.getInstancia().getToken();
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        return builder;
    }

    private HttpResponse<String> enviar(HttpRequest request) {
        try {
            HttpResponse<String> resposta = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (resposta.statusCode() >= 200 && resposta.statusCode() < 300) {
                return resposta;
            }

            throw new ApiException(resposta.statusCode(), extrairMensagemErro(resposta.body()));

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(0, "Falha de conexão com o servidor: " + e.getMessage());
        }
    }

    private <T> T converter(HttpResponse<String> resposta, Class<T> tipo) {
        try {
            if (tipo == Void.class || resposta.body() == null || resposta.body().isBlank()) {
                return null;
            }
            return objectMapper.readValue(resposta.body(), tipo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao interpretar resposta do servidor: " + e.getMessage(), e);
        }
    }

    private <T> T converter(HttpResponse<String> resposta, TypeReference<T> tipo) {
        try {
            return objectMapper.readValue(resposta.body(), tipo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao interpretar resposta do servidor: " + e.getMessage(), e);
        }
    }

    private String extrairMensagemErro(String corpoResposta) {
        try {
            var node = objectMapper.readTree(corpoResposta);
            if (node.has("mensagem")) {
                return node.get("mensagem").asText();
            }
        } catch (Exception ignored) {
        }
        return "Erro na comunicação com o servidor";
    }
}