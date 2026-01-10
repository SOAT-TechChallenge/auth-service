# Auth Service

## Descrição

Este é um microsserviço responsável pela autenticação do projeto TechChallenge. Desenvolvido como parte da fase 5 da pós-graduação em Arquitetura de Software da FIAP, este serviço gerencia a autenticação e autorização de usuários, utilizando JWT para tokens de segurança.

## Tecnologias Utilizadas

- **Java 17**: Linguagem de programação principal.
- **Spring Boot 3.4.5**: Framework para desenvolvimento de aplicações Java.
- **Spring Security**: Para implementação de autenticação e autorização.
- **Spring Data JPA**: Para persistência de dados.
- **MySQL**: Banco de dados relacional.
- **JWT (JSON Web Tokens)**: Para geração e validação de tokens de autenticação.
- **Lombok**: Para redução de código boilerplate.
- **SpringDoc OpenAPI**: Para documentação da API.
- **JaCoCo**: Para análise de cobertura de código.
- **Docker**: Para containerização da aplicação.

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- MySQL 8.0+
- Docker (opcional, para execução em container)

## Instalação e Execução

### Clonando o Repositório

```bash
git clone https://github.com/seu-usuario/auth-service.git
cd auth-service
```

### Configuração do Banco de Dados

1. Certifique-se de que o MySQL está instalado e em execução.
2. Crie um banco de dados chamado `auth_service`.
3. Configure as credenciais no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auth_service
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Executando com Maven

```bash
./mvnw clean install
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

### Executando com Docker

```bash
docker-compose up --build
```

## Uso

### Endpoints Principais

- `POST /auth/login`: Realiza login e retorna um token JWT.
- `POST /auth/register`: Registra um novo usuário.
- `GET /users`: Lista todos os usuários (requer autenticação).

### Documentação da API

A documentação completa da API está disponível via Swagger UI em `http://localhost:8080/swagger-ui.html`.

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/fiap/auth_service/
│   │       ├── AuthServiceApplication.java
│   │       ├── _webApi/
│   │       │   ├── controller/
│   │       │   │   ├── AuthController.java
│   │       │   │   ├── UserWebController.java
│   │       │   │   └── errorHandler/
│   │       │   ├── data/
│   │       │   │   └── persistence/
│   │       │   │       ├── entity/
│   │       │   │       ├── repository/
│   │       │   │       └── UserDataSourceImpl.java
│   │       │   ├── dto/
│   │       │   └── mappers/
│   │       ├── core/
│   │       │   ├── application/
│   │       │   │   ├── domain/
│   │       │   │   ├── dto/
│   │       │   │   └── useCases/
│   │       │   ├── controler/
│   │       │   ├── gateways/
│   │       │   ├── interfaces/
│   │       │   └── presenter/
│   │       └── infrastructure/
│   │           └── security/
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/fiap/auth_service/
            └── AuthServiceApplicationTests.java
```

## Testes

Para executar os testes:

```bash
./mvnw test
```

Para verificar a cobertura de código:

```bash
./mvnw jacoco:report
```

O relatório de cobertura será gerado em `target/site/jacoco/index.html`.

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## Contato

Para dúvidas ou sugestões, entre em contato com a equipe de desenvolvimento.
