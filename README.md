# 🚴‍♂️ iBike Platform

---

# 🔗 Links

- 🌐 Veja a nossa versão web aqui : [iBike Web](https://ibikeweb.onrender.com/login)
- 📋 Veja o Azure Boards: [Azure Boards](https://dev.azure.com/RM555019/iBike)
- 🎥 Veja o vídeo da execução dos [Testes Automatizados](https://youtu.be/zN6p9i_cW2U)

---
# 🧰 Tecnologias Utilizadas
----
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-4169E1?logo=h2&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A2A?logo=apache-maven&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.5-009688?logo=gradle&logoColor=white)

**Plataforma completa iBike** — Backend API + Interface Web para monitoramento e gestão inteligente de pátios de motocicletas.

---

## 🌍 Visão Geral

| 🧩Módulo | 📁Repositório | 📝Descrição |
|--------|-------------|-----------|
| **Backend** | `iBike Backend` | API REST com Spring Boot, Spring Security, H2 (dev), simulação de eventos via MockAPI (Mottu) |
| **Web (UI)** | `iBike Web` | Interface administrativa com Spring Boot + Thymeleaf, CRUD de motos/pátios, perfil de usuário |

> **Atenção**: 💡 Ambos os módulos podem rodar **juntos** (mesmo banco) ou **separadamente** (Web consumindo Backend via API).  
> Esta documentação cobre **os dois projetos em um único README**.

---

## ⚙️ Funcionalidades Completas

| 💼Módulo | 🚀Funcionalidade |
|--------|----------------|
| **Autenticação** | Login com Spring Security (sem JWT) |
| **Dashboard** | Visão por tipo de usuário (Admin / Funcionário) |
| **Motos** | Cadastro, edição, exclusão, listagem com filtros |
| **Pátios** | Criação, edição, desativação, status (disponível, cheio, sobrecarregado) |
| **Eventos** | Entrada/saída simuladas (MockAPI) |
| **Perfil** | Visualizar, editar, desativar conta |
| **Migrações** | Flyway (Web) / JPA DDL-auto (Backend) |

---

## 🧰 Tecnologias

| Tecnologia | Backend | Web |
|-----------|--------|-----|
| Java 17 | Yes | Yes |
| Spring Boot | Yes | Yes |
| Spring Data JPA | Yes | Yes |
| Spring Security | Yes | Yes |
| Thymeleaf | No | Yes |
| H2 (dev) | Yes | No |
| PostgreSQL | Configurable | Yes (padrão) |
| Flyway | No | Yes |
| Lombok | Yes | No |
| Swagger | Yes | No |
| Maven | Yes | No |
| Gradle | No | Yes |

---

## 🧱 Estrutura dos Projetos

### 📦 Backend (`ibike-backend`)

```bash
src/main/
├── java/br/com/fiap/ibike/
│   ├── config/       # Swagger, segurança
│   ├── controller/   # REST Controllers
│   ├── model/        # Entidades JPA
│   ├── repository/   # Repositórios
│   ├── security/     # Spring Security (sem JWT)
│   └── service/      # Lógica de negócio
└── resources/
    ├── application.properties
    └── data.sql      # Dados iniciais (H2)
```

### 💻 Web (`ibike-web`)

```bash
src/main/
├── java/br/com/fiap/iBikeWeb/
│   ├── controller/   # Thymeleaf Controllers
│   ├── service/      # Chamadas JPA (ou API)
│   ├── model/        # Entidades JPA
│   └── config/       # Segurança, etc.
└── resources/
    ├── templates/    # HTML + Thymeleaf
    ├── static/css/   # Estilos
    └── db/migration/ # Flyway
```

---

## 🧩 Como Rodar

### 🧱 Pré-requisitos

- [x] ☕Java 17
- [x] 🧰Maven (`mvnw`) **e/ou** Gradle (`gradlew`)
- [x] 🗃️PostgreSQL (para Web) ou H2 (Backend dev)

---

### ▶️ 1. Backend (API)

```bash
cd ibike-backend
./mvnw spring-boot:run
```

> API: [http://localhost:8080](http://localhost:8080)  
> Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
> H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

### 🖥️ 2. Web (UI)

```bash
cd ibike-web
./gradlew bootRun
```

> UI: [http://localhost:8081](http://localhost:8081) (evite conflito com backend)

---

### 🐳 3. Docker 

Para rodar a aplicação esteja com o Docker Desktop aberto

---

# 🗄️ Configuração do Banco

### PostgreSQL

```properties
# application.properties
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:ibike}
spring.datasource.username=${DB_USER:ibike}
spring.datasource.password=${DB_PASS:ibike}
```

#### O ``` docker-compose.yml ``` com o *Docker Desktop* aberto cuidaram das configurações do banco;

---

## Endpoints da API (Backend)

| Método | Endpoint | Descrição |
|--------|---------|---------|
| `POST` | `/administrador` | Cadastrar admin |
| `PUT` | `/administrador/{id}` | Atualizar próprio perfil |
| `DELETE` | `/administrador/{id}` | Deletar conta |
| `GET` | `/patio` | Listar pátios |
| `POST` | `/patio` | Criar pátio |
| `GET` | `/moto` | Listar motos |
| `GET` | `/monitoramento` | Eventos simulados |

---

## Rotas da UI (Web)

| Método | Rota | Descrição |
|--------|------|---------|
| `GET` | `/login` | Login |
| `GET` | `/home` | Dashboard |
| `GET` | `/motos` | Listar motos |
| `GET` | `/motos/novo` | Nova moto |
| `POST` | `/motos/salvar` | Salvar moto |
| `GET` | `/patios` | Listar pátios |
| `POST` | `/patios/desativar/{id}` | Desativar pátio |
| `GET` | `/usuario` | Perfil |

---

## Integração Web + Backend

- Ambos acessam o mesmo PostgreSQL
- Web usa JPA diretamente
- Simples e rápido

---

## Segurança

- **Spring Security** em ambos
- Usuários **só editam seus próprios dados**
- Preparado para evolução futura


---

## Equipe

| Nome | RM |
|------|----|
| Gabriel Dias Menezes | 555019 |
| Júlia Soares Farias dos Santos | 554609 |
| Sofia Domingues Gonçalves | 554920 |

> **FIAP — Checkpoint Final**

---

## Contribuição

```bash
git checkout -b feature/nova-func
git commit -m "feat: adiciona X"
git push origin feature/nova-func
```

Abra um **Pull Request**!

---
```
