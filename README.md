# ☕ Comunidesk - Backend API

API RESTful desenvolvida com **Java Spring Boot** para gerenciar a lógica de negócio, segurança e persistência de dados da plataforma **Comunidesk**. O sistema conecta comunidades locais, permitindo o gerenciamento de solicitações e ofertas de ajuda.

Este projeto serve como backend para o [Comunidesk Frontend](https://github.com/Rodrigo-Artur/Comunidesk--FrontEnd-/tree/main).

## 🚀 Tecnologias Utilizadas

* **Java 17** - Linguagem base.
* **Spring Boot 3** - Framework principal.
    * **Spring Security** - Autenticação e Autorização via JWT.
    * **Spring Data JPA** - Camada de persistência e ORM.
    * **Spring Web** - Criação de endpoints REST.
* **PostgreSQL** - Banco de dados relacional.
* **JWT (JSON Web Token)** - Padrão para autenticação stateless.
* **Maven** - Gerenciamento de dependências e build.
* **Lombok** - Redução de código boilerplate.

## ✨ Funcionalidades Principais

### 🔒 Segurança & Autenticação
* **Login & Registro:** Endpoints públicos para criação de conta e autenticação (`/auth/login`, `/auth/register`).
* **JWT Stateless:** Geração e validação de tokens para proteger rotas privadas.
* **Controle de Acesso (RBAC):** Diferenciação de permissões entre `USER` e `ADMIN` (definido em `UsuarioRole`).
* **CORS Config:** Configuração global para permitir requisições do Frontend Vue.js.

### 📅 Automação (Background Jobs)
* **Limpeza Automática:** Possui um serviço agendado (`PostExpirationService`) que roda periodicamente para verificar e expirar solicitações antigas, mantendo o quadro limpo automaticamente.

### 📦 Gestão de Conteúdo (Posts)
* **CRUD de Posts:** Criação, leitura e listagem de cards do quadro.
* **Tipagem:** Classificação de posts (Oferta vs. Pedido) via Enum `PostType`.
* **Relacionamentos:** Associação automática de posts ao usuário autenticado.

## 🛠️ Configuração e Execução

### Pré-requisitos
* Java JDK 17+
* Maven
* PostgreSQL instalado e rodando

### Passo a Passo

1.  **Clone o repositório**
    ```bash
    git clone [https://github.com/SEU-USUARIO/comunideskback.git](https://github.com/SEU-USUARIO/comunideskback.git)
    cd comunideskback
    ```

2.  **Configuração do Banco de Dados**
    Abra o arquivo `src/main/resources/application.properties` e configure suas credenciais do PostgreSQL:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/comunidesk_db
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    
    # Configuração do Segredo JWT (Use uma chave forte)
    api.security.token.secret=sua_chave_secreta_aqui
    ```

3.  **Compilar e Rodar**
    ```bash
    ./mvnw spring-boot:run
    ```
    A API estará disponível em: `http://localhost:8080`

## 🔌 Endpoints Principais

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| POST | `/auth/login` | Autentica o usuário e retorna o Token JWT | Público |
| POST | `/auth/register` | Cria uma nova conta de usuário | Público |
| GET | `/posts` | Lista todos os posts ativos | Autenticado |
| POST | `/posts` | Cria um novo post no quadro | Autenticado |

## 🧪 Estrutura do Projeto

O projeto segue o padrão de camadas (Layered Architecture):

```text
src/main/java/com/dsw/comunideskback/
├── config/          # Configurações globais
├── controller/      # Controladores REST (Entry points)
├── dto/             # Objetos de transferência de dados (Request/Response)
├── model/           # Entidades JPA (Banco de Dados)
├── repository/      # Interfaces Spring Data JPA
├── security/        # Configurações de Segurança e Filtros JWT
└── service/         # Regras de Negócio e Services
```

🤝 Autor
Rodrigo Artur

[LinkedIn](https://www.linkedin.com/in/rodrigo-artur-508840336/)

Projeto desenvolvido como parte do portfólio acadêmico em Análise e Desenvolvimento de Sistemas.
