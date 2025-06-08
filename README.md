# Checklist de Conformidade com os Requisitos da Disciplina

| Requisito Técnico                                      | Atendido? | Justificativa / Evidência                                                                                   |
|-------------------------------------------------------|-----------|-------------------------------------------------------------------------------------------------------------|
| Aplicação Web Spring MVC + Thymeleaf                   | Sim       | O projeto possui templates Thymeleaf em `src/main/resources/templates` e controllers para rotas web.         |
| OAuth 2 para autenticação                               | Sim       | Implementado via Spring Security com JWT, conforme classes de segurança e filtros presentes no projeto.     |
| CRUD completo com validação                             | Sim       | Controllers e DTOs para Abrigos, Reservas e Usuários com validação via Spring Validation.                    |
| Internacionalização                                    | Não       | Não foram encontrados arquivos ou configurações específicas para i18n no projeto.                            |
| Integração com Spring AI                               | Sim   | `AiController` para perguntas.          |
| Integração com RabbitMQ (produtor e consumidor)       | Sim       | Configuração RabbitMQ e classes `RabbitMqController` e `RabbitMqConsumer` implementadas.                      |
| Testes unitários e de integração                       | Sim       | Presença de testes em `src/test/java/com/fiap/ponabri/controllers` e outros pacotes.                         |
| Uso de Java 17 e Spring Boot 3.3.0                     | Sim       | Configurado no `pom.xml` e propriedades do projeto.                                                          |
| Uso de banco Oracle com Spring Data JPA                | Sim       | Dependência do driver Oracle e repositórios JPA configurados.                                               |
| Segurança com JWT                                      | Sim       | Implementação de JWT via classes de segurança e filtros.                                                    |
| Comunicação assíncrona com RabbitMQ                    | Sim       | Implementação de produtor e consumidor RabbitMQ.                                                            |
| Interação com IA para respostas simples                | Sim       | `AiController` implementa respostas hardcoded para perguntas específicas.                                   |

---

# Ponabri - Plataforma de Gerenciamento de Abrigos

## Descrição do Projeto

Ponabri é uma plataforma web para gerenciar abrigos comunitários temporários durante eventos extremos, focada em segurança e eficiência. A aplicação facilita o cadastro, reserva e gerenciamento de abrigos, além de fornecer comunicação assíncrona e suporte via inteligência artificial para auxiliar os usuários.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.3.0
- Maven
- Spring Data JPA
- Oracle Database
- Spring Security com JWT
- RabbitMQ
- Spring AI (integração simplificada)
- Lombok
- Spring Validation
- Thymeleaf

## Funcionalidades Principais

- Fluxo de autenticação e autorização via JWT
- CRUD completo para Abrigos e Reservas
- Criação e gerenciamento de Avisos
- Geração de código para dispositivos IoT
- Comunicação assíncrona com RabbitMQ (produtor e consumidor)
- Interação com IA para respostas simples e hardcoded

## Estrutura do Projeto

- `config`: Configurações do projeto, incluindo segurança, RabbitMQ e AI
- `controllers`: Controladores REST e MVC para rotas da aplicação
- `dto`: Objetos de transferência de dados para comunicação entre camadas
- `entities`: Entidades JPA que representam tabelas do banco de dados
- `enums`: Enumerações usadas no projeto
- `exception`: Tratamento global de exceções
- `repositories`: Interfaces JPA para acesso a dados
- `security`: Implementação de segurança JWT e filtros
- `service` e `services`: Lógica de negócio e serviços auxiliares
- `consumers`: Consumidores RabbitMQ
- `src/main/resources/templates/`: Templates Thymeleaf para views
- `src/main/resources/static/`: Arquivos estáticos (JS, CSS, imagens)

## Como Rodar o Projeto

### Pré-requisitos

- Java 17 instalado
- Maven instalado
- Banco de dados Oracle configurado e acessível
- RabbitMQ rodando localmente ou via Docker
- Conta e chave API para Spring AI (mesmo que dummy para testes)

### Passos para Compilar e Iniciar

1. Clone o repositório e navegue até a pasta do projeto.
2. Configure o banco Oracle no arquivo `src/main/resources/application.properties`.
3. Configure a conexão com RabbitMQ no mesmo arquivo.
4. Configure a chave da API do Spring AI em `application.properties`.
5. Execute os comandos:

```bash
mvn clean install
mvn spring-boot:run
```

### URLs Principais

- Login: `http://localhost:8080/login`
- Dashboard: `http://localhost:8080/home`
