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

# Ponabri - Plataforma de Gerenciamento de Abrigos Comunitários Temporários

## Visão Geral

Ponabri é uma plataforma web desenvolvida em Java com Spring Boot, destinada ao gerenciamento eficiente e seguro de abrigos comunitários temporários durante eventos extremos. A aplicação oferece funcionalidades para cadastro, reserva e administração de abrigos, além de comunicação assíncrona via RabbitMQ e suporte básico por inteligência artificial para auxiliar os usuários.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.3.0
- Maven
- Spring Data JPA
- Banco de Dados Oracle
- Spring Security com JWT para autenticação e autorização
- RabbitMQ para comunicação assíncrona
- Spring AI para integração com inteligência artificial
- Lombok para redução de boilerplate
- Spring Validation para validação de dados
- Thymeleaf para templates HTML

## Funcionalidades Principais

- Autenticação e autorização via JWT
- CRUD completo para gerenciamento de abrigos e reservas
- Criação e gerenciamento de avisos relacionados aos abrigos
- Geração de códigos para dispositivos IoT
- Comunicação assíncrona com RabbitMQ (produtor e consumidor)
- Interação com inteligência artificial para respostas simples e hardcoded
- Validação de dados e tratamento global de exceções
- Testes unitários e de integração para garantir qualidade

## Estrutura do Projeto

- `config`: Configurações do sistema, incluindo segurança, RabbitMQ, internacionalização e integração com AI
- `controllers`: Controladores REST e MVC que expõem as rotas da aplicação
- `dto`: Objetos de transferência de dados para comunicação entre camadas
- `entities`: Entidades JPA que representam as tabelas do banco de dados
- `enums`: Enumerações utilizadas no projeto
- `exception`: Tratamento global de exceções
- `repositories`: Interfaces JPA para acesso a dados
- `security`: Implementação de segurança JWT, filtros e utilitários
- `service` e `services`: Lógica de negócio e serviços auxiliares
- `consumers`: Consumidores para filas RabbitMQ
- `src/main/resources/templates/`: Templates Thymeleaf para as views
- `src/main/resources/static/`: Arquivos estáticos (JavaScript, CSS, imagens)

## Como Rodar o Projeto

### Pré-requisitos

- Java 17 instalado
- Maven instalado
- Banco de dados Oracle configurado e acessível
- RabbitMQ rodando localmente ou via Docker
- Conta e chave API para Spring AI

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

## Endpoints Principais

- `/api/auth/register` - Registro de usuários
- `/api/auth/login` - Autenticação e obtenção de token JWT
- `/api/abrigos` - CRUD de abrigos
- `/api/reservas` - CRUD de reservas
- `/api/usuarios` - Gerenciamento de usuários
- `/api/ai` - Interação com inteligência artificial
- `/api/rabbitmq` - Comunicação via RabbitMQ

## Testes

O projeto inclui testes unitários e de integração localizados em `src/test/java/com/fiap/ponabri/`, garantindo a qualidade e estabilidade das funcionalidades.
