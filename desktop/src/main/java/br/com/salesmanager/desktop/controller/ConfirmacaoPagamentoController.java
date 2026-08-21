package br.com.salesmanager.desktop.controller;

import br.com.salesmanager.desktop.MainApp;
import br.com.salesmanager.desktop.http.ApiException;
import br.com.salesmanager.desktop.model.ItemPedido;
import br.com.salesmanager.desktop.service.VendaService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ConfirmacaoPagamentoController {

    @FXML private VBox painelConfirmacao;
    @FXML private VBox painelPagamento;
    @FXML private ListView<String> listaConfirmacao;
    @FXML private Label labelTotalConfirmacao;
    @FXML private Label labelTotalPagamento;
    @FXML private Label labelErroModal;
    @FXML private Button botaoDinheiro;
    @FXML private Button botaoCartao;
    @FXML private Button botaoPix;
    @FXML private Button botaoValeRefeicao;
    @FXML private Button botaoFinalizarVenda;

    private final VendaService vendaService = new VendaService();

    private List<ItemPedido> itens;
    private Stage stage;
    private Runnable aoFinalizarVenda;
    private String formaPagamentoSelecionada;

    public void inicializar(List<ItemPedido> itens, Stage stage, Runnable aoFinalizarVenda) {
        this.itens = itens;
        this.stage = stage;
        this.aoFinalizarVenda = aoFinalizarVenda;

        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            listaConfirmacao.getItems().add(
                    item.getQuantidade() + "x " + item.getProduto().nome()
                            + "  —  " + formatarMoeda(item.getSubtotal()));
            total = total.add(item.getSubtotal());
        }

        String totalFormatado = "Total: " + formatarMoeda(total);
        labelTotalConfirmacao.setText(totalFormatado);
        labelTotalPagamento.setText(totalFormatado);
    }

    @FXML
    private void onVoltarConfirmacaoClicado() {
        stage.close();
    }

    @FXML
    private void onConfirmarClicado() {
        painelConfirmacao.setVisible(false);
        painelConfirmacao.setManaged(false);
        painelPagamento.setVisible(true);
        painelPagamento.setManaged(true);
    }

    @FXML
    private void onVoltarPagamentoClicado() {
        painelPagamento.setVisible(false);
        painelPagamento.setManaged(false);
        painelConfirmacao.setVisible(true);
        painelConfirmacao.setManaged(true);
    }

    @FXML
    private void onFormaPagamentoClicada(javafx.event.ActionEvent evento) {
        Button clicado = (Button) evento.getSource();

        for (Button botao : List.of(botaoDinheiro, botaoCartao, botaoPix, botaoValeRefeicao)) {
            botao.getStyleClass().setAll("forma-pagamento-botao");
        }
        clicado.getStyleClass().setAll("forma-pagamento-selecionada");

        formaPagamentoSelecionada = mapearFormaPagamento(clicado);
        botaoFinalizarVenda.setDisable(false);
        esconderErro();
    }

    private String mapearFormaPagamento(Button botao) {
        if (botao == botaoDinheiro) return "DINHEIRO";
        if (botao == botaoCartao) return "CARTAO";
        if (botao == botaoPix) return "PIX";
        return "VALE_REFEICAO";
    }

    @FXML
    private void onFinalizarVendaClicado() {
        if (formaPagamentoSelecionada == null || botaoFinalizarVenda.isDisabled()) {
            return;
        }

        botaoFinalizarVenda.setDisable(true);
        esconderErro();

        Task<Void> tarefa = new Task<>() {
            @Override
            protected Void call() {
                vendaService.registrar(itens, formaPagamentoSelecionada);
                return null;
            }
        };

        tarefa.setOnSucceeded(e -> {
            stage.close();
            aoFinalizarVenda.run();
        });

        tarefa.setOnFailed(e -> {
            botaoFinalizarVenda.setDisable(false);
            Throwable erro = tarefa.getException();

            if (erro instanceof ApiException apiEx
                    && (apiEx.getStatusCode() == 401 || apiEx.getStatusCode() == 403)) {
                try {
                    stage.close();
                    MainApp.tratarSessaoExpirada();
                } catch (IOException ex) {
                    mostrarErro("Sessão expirada. Reinicie o aplicativo.");
                }
                return;
            }

            String mensagem = (erro instanceof ApiException apiEx)
                    ? apiEx.getMessage()
                    : "Não foi possível registrar a venda";
            mostrarErro(mensagem);
        });

        new Thread(tarefa).start();
    }

    private void mostrarErro(String mensagem) {
        labelErroModal.setText(mensagem);
        labelErroModal.setVisible(true);
        labelErroModal.setManaged(true);
    }

    private void esconderErro() {
        labelErroModal.setVisible(false);
        labelErroModal.setManaged(false);
    }

    private String formatarMoeda(BigDecimal valor) {
        return "R$ " + valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
    }
}