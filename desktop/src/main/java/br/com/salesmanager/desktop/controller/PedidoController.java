package br.com.salesmanager.desktop.controller;

import br.com.salesmanager.desktop.MainApp;
import br.com.salesmanager.desktop.dto.CategoriaResponse;
import br.com.salesmanager.desktop.dto.ProdutoResponse;
import br.com.salesmanager.desktop.http.ApiClient;
import br.com.salesmanager.desktop.http.ApiException;
import br.com.salesmanager.desktop.model.ItemPedido;
import br.com.salesmanager.desktop.service.ProdutoService;
import br.com.salesmanager.desktop.session.SessaoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PedidoController {

    @FXML private HBox listaCategorias;
    @FXML private FlowPane gridProdutos;
    @FXML private ListView<ItemPedido> listaItens;
    @FXML private Label labelTotal;
    @FXML private Label labelAtendente;
    @FXML private Label labelCarrinhoVazio;
    @FXML private HBox faixaErro;
    @FXML private Label labelErroConexao;

    private final ProdutoService produtoService = new ProdutoService();
    private final ObservableList<ItemPedido> carrinho = FXCollections.observableArrayList();
    private Runnable acaoTentarNovamente;

    @FXML
    private void initialize() {
        labelAtendente.setText(SessaoUsuario.getInstancia().getNome());
        listaItens.setItems(carrinho);
        listaItens.setCellFactory(lv -> new ItemPedidoCell());
        atualizarTotal();
        carregarCategorias();
    }

    private void carregarCategorias() {
        Task<List<CategoriaResponse>> tarefa = new Task<>() {
            @Override
            protected List<CategoriaResponse> call() {
                return produtoService.listarCategorias();
            }
        };

        tarefa.setOnSucceeded(e -> {
            esconderErro();
            listaCategorias.getChildren().clear();
            List<CategoriaResponse> categorias = tarefa.getValue();

            for (int i = 0; i < categorias.size(); i++) {
                CategoriaResponse categoria = categorias.get(i);
                Button chip = new Button(categoria.nome());
                chip.getStyleClass().add(i == 0 ? "chip-categoria-ativo" : "chip-categoria");
                chip.setOnAction(ev -> {
                    listaCategorias.getChildren().forEach(no ->
                            no.getStyleClass().setAll("chip-categoria"));
                    chip.getStyleClass().setAll("chip-categoria-ativo");
                    carregarProdutos(categoria.id());
                });
                listaCategorias.getChildren().add(chip);
            }

            if (!categorias.isEmpty()) {
                carregarProdutos(categorias.get(0).id());
            }
        });

        tarefa.setOnFailed(e -> tratarFalhaCarregamento(tarefa.getException(), this::carregarCategorias));
        new Thread(tarefa).start();
    }

    private void carregarProdutos(Long categoriaId) {
        Task<List<ProdutoResponse>> tarefa = new Task<>() {
            @Override
            protected List<ProdutoResponse> call() {
                return produtoService.listarProdutosPorCategoria(categoriaId);
            }
        };

        tarefa.setOnSucceeded(e -> {
            esconderErro();
            gridProdutos.getChildren().clear();
            for (ProdutoResponse produto : tarefa.getValue()) {
                gridProdutos.getChildren().add(criarCardProduto(produto));
            }
        });

        tarefa.setOnFailed(e -> tratarFalhaCarregamento(tarefa.getException(), () -> carregarProdutos(categoriaId)));
        new Thread(tarefa).start();
    }

    private void tratarFalhaCarregamento(Throwable erro, Runnable tentarNovamente) {
        if (sessaoExpirou(erro)) {
            return;
        }
        String mensagem = (erro instanceof ApiException apiEx)
                ? apiEx.getMessage()
                : "Não foi possível conectar ao servidor";
        mostrarErro(mensagem);
        acaoTentarNovamente = tentarNovamente;
    }

    private boolean sessaoExpirou(Throwable erro) {
        if (erro instanceof ApiException apiEx
                && (apiEx.getStatusCode() == 401 || apiEx.getStatusCode() == 403)) {
            try {
                MainApp.tratarSessaoExpirada();
            } catch (IOException ex) {
                System.err.println("Erro ao redirecionar após sessão expirada: " + ex.getMessage());
            }
            return true;
        }
        return false;
    }

    private void mostrarErro(String mensagem) {
        labelErroConexao.setText(mensagem);
        faixaErro.setVisible(true);
        faixaErro.setManaged(true);
    }

    private void esconderErro() {
        faixaErro.setVisible(false);
        faixaErro.setManaged(false);
    }

    @FXML
    private void onTentarNovamenteClicado() {
        if (acaoTentarNovamente != null) {
            esconderErro();
            acaoTentarNovamente.run();
        }
    }

    private VBox criarCardProduto(ProdutoResponse produto) {
        VBox card = new VBox(8);
        card.getStyleClass().add("produto-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(120);
        card.setOnMouseClicked(e -> adicionarAoCarrinho(produto));

        Node imagem = criarImagemProduto(produto);

        Label nome = new Label(produto.nome());
        nome.getStyleClass().add("produto-nome");
        nome.setWrapText(true);
        nome.setPrefWidth(110);

        Label preco = new Label(formatarMoeda(produto.preco()));
        preco.getStyleClass().add("produto-preco");

        card.getChildren().addAll(imagem, nome, preco);
        return card;
    }

    private Node criarImagemProduto(ProdutoResponse produto) {
        if (produto.imagemUrl() != null && !produto.imagemUrl().isBlank()) {
            String urlCompleta = ApiClient.SERVER_URL + produto.imagemUrl();
            Image imagem = br.com.salesmanager.desktop.http.ImagemCache.obter(urlCompleta, 100, 70);

            ImageView imageView = new ImageView(imagem);
            imageView.setFitWidth(100);
            imageView.setFitHeight(70);
            imageView.setPreserveRatio(false);
            imageView.getStyleClass().add("produto-imagem");

            Rectangle clip = new Rectangle(100, 70);
            clip.setArcWidth(16);
            clip.setArcHeight(16);
            imageView.setClip(clip);

            return imageView;
        }

        Label icone = new Label("🍽");
        icone.getStyleClass().add("produto-icone");
        return icone;
    }

    private void adicionarAoCarrinho(ProdutoResponse produto) {
        for (ItemPedido item : carrinho) {
            if (item.getProduto().id().equals(produto.id())) {
                item.incrementar();
                listaItens.refresh();
                atualizarTotal();
                return;
            }
        }
        carrinho.add(new ItemPedido(produto, 1));
        atualizarTotal();
    }

    private void atualizarTotal() {
        boolean vazio = carrinho.isEmpty();
        labelCarrinhoVazio.setVisible(vazio);
        labelCarrinhoVazio.setManaged(vazio);
        listaItens.setVisible(!vazio);

        BigDecimal total = carrinho.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        labelTotal.setText("Total: " + formatarMoeda(total));
    }

    private String formatarMoeda(BigDecimal valor) {
        return "R$ " + valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
    }

    @FXML
    private void onFinalizarClicado() {
        if (carrinho.isEmpty()) {
            return;
        }

        try {
            MainApp.abrirModalConfirmacao(List.copyOf(carrinho), () -> {
                carrinho.clear();
                atualizarTotal();
            });
        } catch (IOException e) {
            System.err.println("Erro ao abrir modal de confirmação: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelarClicado() {
        if (carrinho.isEmpty()) {
            return;
        }

        Alert alerta = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Isso vai remover todos os itens do pedido atual. Deseja continuar?",
                ButtonType.CANCEL, ButtonType.OK);
        alerta.setTitle("Cancelar pedido");
        alerta.setHeaderText(null);

        alerta.showAndWait().ifPresent(botao -> {
            if (botao == ButtonType.OK) {
                carrinho.clear();
                atualizarTotal();
            }
        });
    }

    @FXML
    private void onSairClicado() throws IOException {
        SessaoUsuario.getInstancia().encerrar();
        MainApp.trocarTela("/view/login.fxml", 400, 480, false);
    }

    private class ItemPedidoCell extends ListCell<ItemPedido> {

        private final HBox container = new HBox(8);
        private final Label labelDescricao = new Label();
        private final Region espaco = new Region();
        private final Button botaoRemover = new Button("−");

        ItemPedidoCell() {
            HBox.setHgrow(espaco, Priority.ALWAYS);
            botaoRemover.getStyleClass().add("carrinho-remover");
            container.setAlignment(Pos.CENTER_LEFT);
            container.getChildren().addAll(labelDescricao, espaco, botaoRemover);

            botaoRemover.setOnAction(e -> {
                ItemPedido item = getItem();
                if (item == null) return;

                item.decrementar();
                if (item.getQuantidade() == 0) {
                    carrinho.remove(item);
                } else {
                    listaItens.refresh();
                }
                atualizarTotal();
            });
        }

        @Override
        protected void updateItem(ItemPedido item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                labelDescricao.setText(item.getQuantidade() + "x " + item.getProduto().nome()
                        + " - " + formatarMoeda(item.getSubtotal()));
                setGraphic(container);
            }
        }
    }
}