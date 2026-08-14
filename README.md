# Sales Manager

Sistema de ponto de venda para lanchonete, com aplicativo desktop (JavaFX) para operação e painel web para gestão.

## Visão geral

O sistema é dividido em três perfis de acesso:

| Perfil | Onde acessa | Responsabilidade |
|---|---|---|
| Administrador | Web | Cadastra gerentes e atendentes |
| Gerente | Desktop + Web | Cadastra produtos e categorias · acompanha o dashboard de vendas |
| Atendente | Desktop | Realiza vendas pela tela de pedido |

Um backend central concentra as regras de negócio e o acesso ao banco de dados, servindo tanto o aplicativo desktop quanto o painel web.

## Arquitetura

```
                     MySQL
                       │
                Backend (API REST)
             ┌──────────┴──────────┐
             │                     │
     Desktop (JavaFX)          Web
     · Cadastro de produtos    · Cadastro de usuários (admin)
     · Tela de pedido          · Dashboard de vendas (gerente)
```

O desktop e a web não acessam o banco diretamente — ambos consomem a mesma API.

## Stack

- **Backend:** Java 21, Spring Boot, Spring Data JPA
- **Banco de dados:** MySQL
- **Desktop:** JavaFX + Scene Builder (FXML/CSS)
- **Comunicação:** REST / JSON
- **Web:** a definir

## Modelo de dados

`usuario` (papel: admin/gerente/atendente) · `categoria` · `produto` · `venda` · `item_venda`

Script completo: [`schema.sql`](./schema.sql)

## Escopo da v1

Incluído: cadastro de usuários, produtos e categorias · tela de pedido · dashboard de vendas (total, gráfico, mais vendidos)

Fora do escopo: controle de estoque · cadastro de cliente · relatórios avançados

## Roadmap

1. Backend (Spring Boot) — entidades, endpoints, autenticação
2. Banco de dados (MySQL)
3. Desktop (JavaFX) — login, cadastro de produtos, tela de pedido
4. Web — cadastro de usuários, dashboard de vendas

## Licença

Definir antes da publicação (ex.: MIT).
