package br.com.salesmanager.desktop;

import br.com.salesmanager.desktop.controller.ConfirmacaoPagamentoController;
import br.com.salesmanager.desktop.controller.LoginController;
import br.com.salesmanager.desktop.model.ItemPedido;
import br.com.salesmanager.desktop.session.SessaoUsuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainApp extends Application {

    private static Stage stagePrincipal;

    @Override
    public void start(Stage stage) throws IOException {
        stagePrincipal = stage;
        stage.setTitle("Sales Manager");
        trocarTela("/view/login.fxml", 400, 480, false);
        stage.show();
    }

    public static void trocarTela(String caminhoFxml, double largura, double altura, boolean redimensionavel) throws IOException {
        trocarTela(caminhoFxml, largura, altura, redimensionavel, false);
    }

    public static void trocarTela(String caminhoFxml, double largura, double altura, boolean redimensionavel, boolean maximizado) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(caminhoFxml));
        Parent root = loader.load();
        Scene scene = new Scene(root, largura, altura);
        scene.getStylesheets().add(MainApp.class.getResource(
                caminhoFxml.replace(".fxml", ".css")).toExternalForm());

        stagePrincipal.setScene(scene);
        stagePrincipal.setResizable(redimensionavel);
        stagePrincipal.setMaximized(false);
        stagePrincipal.setWidth(largura);
        stagePrincipal.setHeight(altura);
        stagePrincipal.centerOnScreen();

        if (redimensionavel) {
            stagePrincipal.setMinWidth(760);
            stagePrincipal.setMinHeight(500);
        } else {
            stagePrincipal.setMinWidth(0);
            stagePrincipal.setMinHeight(0);
        }

        if (maximizado) {
            stagePrincipal.setMaximized(true);
        }
    }

    public static void abrirModalConfirmacao(List<ItemPedido> itens, Runnable aoFinalizarVenda) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/confirmacao_pagamento.fxml"));
        Parent root = loader.load();

        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(stagePrincipal);
        modal.setTitle("Confirmar Pedido");
        modal.setResizable(false);

        Scene scene = new Scene(root, 380, 480);
        scene.getStylesheets().add(MainApp.class.getResource("/view/confirmacao_pagamento.css").toExternalForm());
        modal.setScene(scene);

        ConfirmacaoPagamentoController controller = loader.getController();
        controller.inicializar(itens, modal, aoFinalizarVenda);

        modal.showAndWait();
    }

    public static void tratarSessaoExpirada() throws IOException {
        SessaoUsuario.getInstancia().encerrar();
        LoginController.definirMensagemPendente("Sessão expirada. Faça login novamente.");
        trocarTela("/view/login.fxml", 400, 480, false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}