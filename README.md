# Microservices Java

Projeto desenvolvido utilizando arquitetura de microsserviços com Java e Spring Boot, com foco na separação de responsabilidades, comunicação entre serviços e execução distribuída.

## Tecnologias Utilizadas

* Java
* Spring Boot
* Spring Cloud
* Maven
* Docker
* Docker Compose
* PostgreSQL
* REST API

## Estrutura do Projeto

```bash id="q2ok0k"
microservices-java/
│
├── gateway/
├── discovery-server/
├── service-1/
├── service-2/
└── docker-compose.yml
```

## Funcionalidades

* Arquitetura baseada em microsserviços
* Comunicação entre serviços via API REST
* API Gateway
* Service Discovery
* Containerização com Docker
* Orquestração com Docker Compose

## Execução do Projeto

### Clonar o repositório

```bash id="8o3b2j"
git clone https://github.com/marian4melara7/microservices-java.git
```

```bash id="q4yo97"
cd microservices-java
```

### Executar com Docker Compose

```bash id="n8z2yn"
docker compose up --build
```

## Requisitos

* Java 17+
* Maven
* Docker
* Docker Compose

## Objetivo

O projeto tem como objetivo aplicar conceitos de sistemas distribuídos e arquitetura de microsserviços, utilizando ferramentas modernas para desenvolvimento, conteinerização e comunicação entre serviços.
