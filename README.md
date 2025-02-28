# Projeto Fin - Sistema de Gestão de Ativos Financeiros

## Visão Geral
Este projeto implementa um sistema de gestão de ativos financeiros que permite aos usuários gerenciar
seus investimentos e acompanhar o crescimento de seu patrimônio,
incluindo a gestão de datas nas operações financeiras.

## Como Executar o Projeto

### Pré-requisitos
- Java 11
- Gradle

### Passos
- Clone o repositório
- Execute o comando: `./gradlew bootRun`
- Acesse a API em: http://localhost:8080
- Acesse o console H2 em: http://localhost:8080/h2-console
  Acesso ao Console H2
  URL: http://localhost:8080/h2-console

### Dados para login no H2 

JDBC URL: jdbc:h2:mem:findb

Username: sa

Password: (deixar em branco)

## Tecnologias Utilizadas

### Backend
- Java 11: Linguagem de programação principal
- Spring Boot 2.7.8: Framework para desenvolvimento de aplicações Java
- Spring Data JPA: Facilita o acesso e manipulação de dados
- H2 Database: Banco de dados relacional em memória
- Lombok: Reduz código boilerplate
- Gradle: Ferramenta de automação de build

### Arquitetura
O projeto segue uma arquitetura em camadas (Layered Architecture) com o padrão MVC adaptado para APIs REST:

- Controller: Recebe requisições HTTP e retorna respostas
- Service: Contém a lógica de negócio
- Repository: Responsável pela persistência e acesso aos dados
- Model: Define as entidades e DTOs do sistema

## Funcionalidades Implementadas

### Gestão de Conta Corrente
- Consulta de saldo por data
- Lançamentos de crédito e débito com data
- Validação para evitar saldo negativo em qualquer data

### Gestão de Ativos
- CRUD completo de ativos com datas de emissão e vencimento
- Registro de valores de mercado por data
- Validações específicas para datas (dias úteis, período válido)

### Movimentações
- Compra e venda de ativos com data
- Validações de quantidade disponível
- Impacto automático no saldo da conta corrente

### Consultas
- Posição de ativos por data
- Lançamentos por período
- Movimentações por período

### Justificativa da Escolha do H2
- O H2 foi escolhido como banco de dados por ser:
- Embarcado: Não requer instalação externa
- Em memória: Rápido e eficiente para desenvolvimento e testes
- Compatível com JPA: Suporta todas as funcionalidades necessárias
- Console web: Facilita a visualização e manipulação dos dados

## Endpoints REST

### Conta Corrente
- GET /conta-corrente/saldo?contaId={id}&data={data}: Consulta saldo em uma data
- POST /conta-corrente/credito?contaId={id}: Registra crédito
- POST /conta-corrente/debito?contaId={id}: Registra débito
- GET /conta-corrente/lancamentos?contaId={id}&dataInicio={inicio}&dataFim={fim}: Lista lançamentos

### Ativos
- GET /ativos: Lista todos os ativos
- GET /ativos/{nome}: Busca ativo por nome
- POST /ativos: Cadastra novo ativo
- PUT /ativos/{nome}: Atualiza ativo
- DELETE /ativos/{nome}: Remove ativo
- GET /ativos/posicao?data={data}: Consulta posição dos ativos

### Movimentações
- POST /movimentacoes/comprar: Registra compra de ativo
- POST /movimentacoes/vender: Registra venda de ativo
- GET /movimentacoes?ativo={ativo}&dataInicio={inicio}&dataFim={fim}: Lista movimentações

### Valor de Mercado
- POST /valor-mercado: Registra valor de mercado
- DELETE /valor-mercado/{id}: Remove valor de mercado

## HTTP Status Codes
- 200 OK: Requisição bem-sucedida
- 400 Bad Request: Erro de validação ou dados inválidos
- 404 Not Found: Recurso não encontrado
- 500 Internal Server Error: Erro interno do servidor
