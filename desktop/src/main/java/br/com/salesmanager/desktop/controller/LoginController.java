package br.com.salesmanager.desktop.controller;

import br.com.salesmanager.desktop.MainApp;
import br.com.salesmanager.desktop.http.ApiException;
import br.com.salesmanager.desktop.service.AuthService;
import br.com.salesmanager.desktop.session.SessaoUsuario;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private Label labelErro;
    @FXML private Button botaoEntrar;

    private final AuthService authService = new AuthService();

    private static String mensagemPendente;

    public static void definirMensagemPendente(String mensagem) {
        mensagemPendente = mensagem;
    }

    @FXML
    private void initialize() {
        if (mensagemPendente != null) {
            mostrarErro(mensagemPendente);
            mensagemPendente = null;
        }
    }

    @FXML
    private void onEntrarClicado() {
        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText();

        if (email.isBlank() || senha.isBlank()) {
            mostrarErro("Preencha e-mail e senha");
            return;
        }

        botaoEntrar.setDisable(true);
        esconderErro();

        Task<Void> tarefaLogin = new Task<>() {
            @Override
            protected Void call() {
                authService.login(email, senha);
                return null;
            }
        };

        tarefaLogin.setOnSucceeded(e -> {
            botaoEntrar.setDisable(false);
            String papel = SessaoUsuario.getInstancia().getPapel();

            if (!"ATENDENTE".equals(papel)) {
                SessaoUsuario.getInstancia().encerrar();
                mostrarErro("Este aplicativo é exclusivo para atendentes. Administradores e gerentes devem usar o painel web.");
                return;
            }

            try {
                MainApp.trocarTela("/view/pedido.fxml", 900, 650, true, true);
            } catch (IOException ex) {
                mostrarErro("Erro ao abrir a tela principal: " + ex.getMessage());
            }
        });

        tarefaLogin.setOnFailed(e -> {
            botaoEntrar.setDisable(false);
            Throwable erro = tarefaLogin.getException();
            String mensagem = (erro instanceof ApiException apiEx)
                    ? apiEx.getMessage()
                    : "Não foi possível conectar ao servidor";
            mostrarErro(mensagem);
        });

        new Thread(tarefaLogin).start();
    }

    private void mostrarErro(String mensagem) {
        labelErro.setText(mensagem);
        labelErro.setVisible(true);
        labelErro.setManaged(true);
    }

    private void esconderErro() {
        labelErro.setVisible(false);
        labelErro.setManaged(false);
    }
}