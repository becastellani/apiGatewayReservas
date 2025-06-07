# 🏢 Sistema de Microserviços - Gestão de Reservas

Sistema distribuído de gestão de usuários, salas e reservas utilizando arquitetura de microserviços com mensageria assíncrona.

## 🔍 Visão Geral

O sistema é composto por três microserviços independentes que se comunicam através de mensageria assíncrona (RabbitMQ) e são orquestrados por um API Gateway. Cada microserviço possui seu próprio banco de dados PostgreSQL, seguindo o padrão Database per Service.

### Funcionalidades Principais

- **Gestão de Usuários**: Cadastro, consulta e manutenção de usuários
- **Gestão de Salas**: Controle de salas disponíveis para reserva
- **Gestão de Reservas**: Sistema de reservas com cache de usuários para performance
- **Comunicação Assíncrona**: Sincronização automática de dados entre microserviços
- **API Gateway**: Ponto único de entrada para todas as requisições

## 🏗️ Arquitetura

```
┌─────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Cliente   │───▶│   API Gateway   │───▶│  Microserviços  │
└─────────────┘    │   (Port 8080)   │    │                 │
                   └─────────────────┘    │ UserService     │
                                          │ SalaService     │
                                          │ ReservaService  │
                                          └─────────────────┘
                                                   │
                   ┌─────────────────┐            │
                   │    RabbitMQ     │◀───────────┘
                   │  (Mensageria)   │
                   └─────────────────┘
                           
                   ┌─────────────────┐
                   │   PostgreSQL    │
                   │ (3 Databases)   │
                   └─────────────────┘
```

### Padrões Implementados

- **Database per Service**: Cada microserviço possui seu próprio banco de dados
- **API Gateway Pattern**: Ponto único de entrada para clientes externos
- **Event-Driven Architecture**: Comunicação assíncrona via eventos
- **Saga Pattern**: Coordenação de transações distribuídas
- **Cache Pattern**: Replicação de dados para melhor performance

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Cloud Gateway 2024.0.0**
- **Spring Data JPA**
- **Spring AMQP (RabbitMQ)**

### Bancos de Dados
- **PostgreSQL 15**
- **Adminer** (Interface web para administração)

### Mensageria
- **RabbitMQ 3** (com Management UI)

### Containerização
- **Docker & Docker Compose**
- **Maven** (Build tool)

### Arquitetura
- **Domain-Driven Design (DDD)**
- **Arquitetura Hexagonal**
- **Microserviços**

## 🚀 Microserviços

### 1. UserService (Porta 8081)
**Responsabilidade**: Gestão completa de usuários

**Recursos**:
- Cadastro de usuários com validações
- Consulta e atualização de dados
- Publicação de eventos para outros serviços
- Banco: `usersdb` (Porta 5433)

**Domínio**:
```java
User {
  id, nome, email, senha, telefone, 
  endereco, cpf, dataNascimento, dataCadastro
}
```

### 2. SalaService (Porta 8082)
**Responsabilidade**: Controle de salas e disponibilidade

**Recursos**:
- Cadastro e gestão de salas
- Controle de disponibilidade
- Banco: `salasdb` (Porta 5434)

### 3. ReservaService (Porta 8083)
**Responsabilidade**: Sistema de reservas

**Recursos**:
- Criação e gestão de reservas
- Cache local de usuários (sincronizado via eventos)
- Validações de disponibilidade
- Banco: `reservasdb` (Porta 5435)

**Cache Pattern**: Mantém uma cópia local dos dados de usuários para evitar dependências síncronas entre serviços.

### 4. Gateway (Porta 8080)
**Responsabilidade**: Roteamento e orquestração

**Recursos**:
- Ponto único de entrada
- Roteamento inteligente
- Balanceamento de carga
- Configuração de CORS

## 📋 Pré-requisitos

- **Docker** e **Docker Compose**
- **Git**
- **8GB RAM** (recomendado)
- **Portas disponíveis**: 8080-8083, 5432-5435, 5672, 15672

## 🚀 Como Executar

### 1. Clone o Repositório
```bash
git clone <url-do-repositorio>
cd microservicos-projeto
```

### 2. Execute com Docker Compose
```bash
# Construir e executar todos os serviços
docker-compose up --build

# Ou executar em background
docker-compose up --build -d
```

### 3. Aguardar Inicialização
Os serviços podem levar 2-3 minutos para estar completamente operacionais.

### 4. Verificar Status
```bash
# Verificar se todos os containers estão rodando
docker-compose ps

# Verificar logs
docker-compose logs -f
```

## 📡 Endpoints da API

### Acessar via API Gateway (Porta 8080)

#### Usuários
```http
GET    /users          # Listar usuários
POST   /users          # Criar usuário
GET    /users/{id}     # Buscar usuário por ID
PUT    /users/{id}     # Atualizar usuário
DELETE /users/{id}     # Deletar usuário
```

#### Salas
```http
GET    /salas          # Listar salas
POST   /salas          # Criar sala
GET    /salas/{id}     # Buscar sala por ID
PUT    /salas/{id}     # Atualizar sala
DELETE /salas/{id}     # Deletar sala
```

#### Reservas
```http
GET    /reservas       # Listar reservas
POST   /reservas       # Criar reserva
GET    /reservas/{id}  # Buscar reserva por ID
PUT    /reservas/{id}  # Atualizar reserva
DELETE /reservas/{id}  # Deletar reserva
```

### Exemplo de Requisição

#### Criar Usuário
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "nome": {"nome": "João Silva"},
    "email": {"email": "joao@teste.com"},
    "senha": "123456",
    "telefone": {"telefone": "11999999999"},
    "cpf": {"cpf": "12345678901"},
    "dataNascimento": "1990-01-01",
    "dataCadastro": "2024-06-02"
  }'
```

## 📨 Mensageria

### Fluxo de Eventos

1. **UserService** cria um usuário
2. Publica evento `UserCreated` na fila `user.created.queue`
3. **ReservaService** consome o evento
4. Cria uma cópia local do usuário (cache)
5. Usuário fica disponível para reservas sem chamadas HTTP

### Filas RabbitMQ
- `user.created.queue` - Eventos de criação de usuários

### Benefícios
- **Performance**: Evita chamadas síncronas entre serviços
- **Resiliência**: Serviços funcionam independentemente
- **Escalabilidade**: Processamento assíncrono de eventos

## 📊 Monitoramento

### URLs de Monitoramento

| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **API Gateway** | http://localhost:8080/actuator/health | - |
| **RabbitMQ Management** | http://localhost:15672 | admin/admin |
| **Adminer (DB)** | http://localhost:8090 | postgres/admin |
| **UserService Health** | http://localhost:8081/actuator/health | - |
| **SalaService Health** | http://localhost:8082/actuator/health | - |
| **ReservaService Health** | http://localhost:8083/actuator/health | - |

### Logs
```bash
# Ver logs de todos os serviços
docker-compose logs

# Ver logs específicos
docker-compose logs userservice
docker-compose logs gateway
docker-compose logs rabbitmq
```

## 📁 Estrutura do Projeto

```
projeto/
├── docker-compose.yml
├── README.md
├── gateway/
│   └── demo/
│       ├── src/main/java/
│       ├── pom.xml
│       └── Dockerfile
├── userservice/
│   └── demo/
│       ├── src/main/java/
│       │   └── com/userservice/demo/
│       │       ├── domain/
│       │       ├── infrastructure/
│       │       ├── application/
│       │       └── interfaces/
│       ├── pom.xml
│       └── Dockerfile
├── salaservice/
│   └── demo/
│       ├── src/main/java/
│       ├── pom.xml
│       └── Dockerfile
└── reservaservice/
    └── demo/
        ├── src/main/java/
        │   └── com/reservaservice/demo/
        │       ├── domain/
        │       ├── infrastructure/
        │       ├── application/
        │       └── interfaces/
        ├── pom.xml
        └── Dockerfile
```

### Padrão DDD por Microserviço
```
src/main/java/com/[service]/demo/
├── domain/
│   ├── model/          # Entidades de domínio
│   ├── repository/     # Interfaces de repositório
│   └── service/        # Serviços de domínio
├── infrastructure/
│   ├── config/         # Configurações (RabbitMQ, DB)
│   ├── messaging/      # Publishers e Consumers
│   └── persistence/    # Implementações JPA
├── application/
│   └── service/        # Serviços de aplicação
```

## 🔧 Comandos Úteis

### Docker
```bash
# Parar todos os serviços
docker-compose down

# Reconstruir apenas um serviço
docker-compose up --build userservice

# Ver logs em tempo real
docker-compose logs -f

# Limpar volumes (CUIDADO: remove dados)
docker-compose down -v
```

### Desenvolvimento
```bash
# Acessar container para debug
docker-compose exec userservice sh

# Verificar conectividade entre containers
docker-compose exec gateway ping userservice
```

## 🎯 Arquitetura de Qualidade

### Princípios Aplicados
- **Single Responsibility**: Cada microserviço tem uma responsabilidade específica
- **Loose Coupling**: Comunicação assíncrona reduz acoplamento
- **High Cohesion**: Funcionalidades relacionadas agrupadas no mesmo serviço
- **Fail Fast**: Validações próximas à entrada dos dados
- **Circuit Breaker**: Resiliência entre serviços

### Padrões de Microserviços
- ✅ Database per Service
- ✅ API Gateway
- ✅ Event Sourcing
- ✅ Saga Pattern
- ✅ Cache-Aside Pattern



Desenvolvido como projeto acadêmico para demonstração de conceitos de microserviços e arquitetura distribuída.

---

**🎓 Projeto Acadêmico - Microserviços**  
*Demonstração de arquitetura distribuída com Spring Boot, RabbitMQ e Docker*
