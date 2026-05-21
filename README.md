<p align="center">
  <img src="logo.svg" alt="Management Fleet Logo" width="120"/>
</p>

[![Java](https://img.shields.io/badge/Java-21-blue)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.9-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)](https://www.postgresql.org/)
[![Build](https://github.com/bsouza1535/ManagmentFleet/actions/workflows/build.yml/badge.svg)](https://github.com/bsouza1535/ManagmentFleet/actions/workflows/build.yml)
[![Coverage](https://img.shields.io/badge/coverage-passing-brightgreen)](https://about.codecov.io/) <!-- Ajuste para Codecov real -->
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=bsouza1535_ManagmentFleet&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=bsouza1535_ManagmentFleet)

---

# Management Fleet

Sistema de Gestão de Frotas

## Descrição

O **Management Fleet** é um sistema completo para gestão de frotas, permitindo o cadastro, consulta e gerenciamento de veículos, condutores, manutenções e abastecimentos. O projeto utiliza Spring Boot, Spring Data JPA, PostgreSQL, autenticação JWT e Flyway para versionamento do banco de dados.

## Exemplo de Requisição/Resposta

### Criar Veículo

**POST** `/api/vehicles`
```json
{
  "licensePlate": "ABC1234",
  "model": "Onix",
  "manufacturer": "Chevrolet",
  "year": 2022,
  "status": "ACTIVE",
  "typeofcar": "HATCH",
  "mileage": 10000,
  "nextMaintenanceMileage": 15000,
  "rentalValue": 1200.0
}
```
**Resposta:**
```json
{
  "licensePlate": "ABC1234",
  "model": "Onix",
  "manufacturer": "Chevrolet",
  "year": 2022,
  "status": "ACTIVE",
  "typeofcar": "HATCH",
  "createdAt": "2026-05-21T10:00:00",
  "mileage": 10000,
  "nextMaintenanceMileage": 15000,
  "rentalValue": 1200.0
}
```

### Listar Condutores

**GET** `/api/drivers?name=Joao`

**Resposta:**
```json
{
  "content": [
    {
      "name": "João Silva",
      "cnh": "12345678900",
      "cnhExpirationDate": "2028-01-01",
      "status": "ACTIVE",
      "createdAt": "2026-05-21T10:00:00"
    }
  ],
  "pageable": { /* ... */ },
  "totalElements": 1
}
```

---

## Como contribuir

Consulte o arquivo [CONTRIBUTING.md](CONTRIBUTING.md) para orientações detalhadas.

## Licença

Este projeto está sob a licença MIT.

---

### Dicas para tornar o projeto mais chamativo no GitHub

- Adicione badges de build, cobertura de testes e qualidade de código (ex: GitHub Actions, Codecov, SonarCloud)
- Inclua screenshots ou GIFs mostrando a interface (caso tenha front-end ou use Swagger)
- Crie um logo simples para o projeto e adicione ao README
- Escreva um arquivo `CONTRIBUTING.md` com orientações para novos contribuidores
- Adicione exemplos de requisições/respostas da API no README
- Configure GitHub Actions para build/teste automático
- Ative o GitHub Pages para documentação
- Use issues e projetos do GitHub para organizar o roadmap
