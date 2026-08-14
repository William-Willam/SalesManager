# Sales Manager

Sistema de vendas para lanchonete: aplicativo desktop (JavaFX) dedicado à operação de vendas, e painel web (React) para administração e gestão.

## Visão geral

| Perfil | Onde acessa | Responsabilidade |
|---|---|---|
| Administrador | Web | Cadastra gerentes e atendentes |
| Gerente | Web | Cadastra produtos e categorias · acompanha o dashboard de vendas |
| Atendente | Desktop | Realiza vendas |

Um backend central concentra as regras de negócio e o acesso ao banco de dados, servindo tanto o desktop quanto a web.

## Arquitetura

```
                     MySQL
                       │
                Backend (API REST)
             ┌──────────┴──────────┐
             │                     │
     Desktop (JavaFX)          Web (React)
     Exclusivo para vendas     · Admin: cadastro de usuários
                                · Gerente: produtos/categorias
                                · Gerente: dashboard de vendas
```

Desktop e web não acessam o banco diretamente — ambos consomem a mesma API.

## Stack

- **Backend:** Java 21, Spring Boot, Spring Data JPA
- **Banco de dados:** MySQL
- **Desktop:** JavaFX + Scene Builder (FXML/CSS)
- **Web:** React
- **Comunicação:** REST / JSON

## Fluxo de venda (desktop)

O desktop é dedicado exclusivamente ao fluxo de venda, com três telas em sequência:

1. **Tela de Pedido** — cards de produto agrupados por categoria; o atendente monta o carrinho.
2. **Tela de Confirmação** — discrimina os produtos escolhidos (item, quantidade, preço, subtotal) para conferência antes de fechar, permitindo eventual troca.
3. **Forma de Pagamento** — após confirmar, o atendente escolhe entre Dinheiro, Cartão, Pix ou Vale-refeição e a venda é finalizada.

## Modelo de dados

`usuario` (papel: admin/gerente/atendente) · `categoria` · `produto` · `venda` (com forma de pagamento) · `item_venda`

## Escopo da v1

**Incluído:** cadastro de usuários, produtos e categorias · fluxo completo de venda (pedido → confirmação → pagamento) · dashboard de vendas (total, gráfico, produtos mais vendidos)

**Fora do escopo:** controle de estoque · cadastro de cliente · relatórios avançados/exportação

## Roadmap

1. Backend (Spring Boot) — entidades, endpoints REST, autenticação por papel
2. Banco de dados (MySQL)
3. Desktop (JavaFX) — login do atendente, tela de pedido, confirmação e pagamento
4. Web (React) — login, área do administrador, área do gerente (produtos + dashboard)

## Licença

Definir antes da publicação (ex.: MIT).
