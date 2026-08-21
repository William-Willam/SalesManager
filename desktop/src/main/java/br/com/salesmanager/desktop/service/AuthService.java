package br.com.salesmanager.desktop.service;

import br.com.salesmanager.desktop.dto.LoginRequest;
import br.com.salesmanager.desktop.dto.LoginResponse;
import br.com.salesmanager.desktop.http.ApiClient;
import br.com.salesmanager.desktop.session.SessaoUsuario;

public class AuthService {

    private final ApiClient apiClient = new ApiClient();

    public void login(String email, String senha) {
        LoginResponse resposta = apiClient.post("/login", new LoginRequest(email, senha), LoginResponse.class);

        SessaoUsuario.getInstancia().iniciar(
                resposta.token(),
                resposta.nome(),
                resposta.email(),
                resposta.papel()
        );
    }
}