package br.com.salesmanager.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    @jakarta.validation.constraints.NotBlank(message = "nome é obrigatório")
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    @jakarta.validation.constraints.NotNull(message = "preço é obrigatório")
    @jakarta.validation.constraints.Positive(message = "preço deve ser maior que zero")
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @jakarta.validation.constraints.NotNull(message = "categoria é obrigatória")
    private Categoria categoria;

    @Column(name = "imagem_url", length = 255)
    private String imagemUrl;

    public Produto() {
    }

    public Produto(String nome, String descricao, BigDecimal preco, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}