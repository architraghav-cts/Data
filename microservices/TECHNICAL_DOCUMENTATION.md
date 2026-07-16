# Microservices Project - Technical Documentation

## 1) Project Overview

This project is a Spring-based microservices system composed of:

- `quizservice` (domain microservice)
- `questionservice` (domain microservice)
- `apigateway` (single entry point)
- `eurekaserver` (service discovery)
- `configserver` (centralized externalized configuration)

### 1.1 High-Level Architecture (Diagram Explanation)

```text
Client
  |
  v
API Gateway (apigateway)
  |
  |----(service discovery via Eureka)---->
  |                                        Eureka Server (eurekaserver)
  |
  |----> quizservice ----(Feign REST call)----> questionservice
  |
  +----> questionservice

All services (gateway + microservices + eureka) fetch external config from:
Config Server (configserver) -> Git-backed config repository
```

### 1.2 Purpose and Responsibilities

- **`configserver`**
  - Central source of configuration for all services.
  - Pulls configuration from a remote Git repository.
  - Reduces duplication and allows centralized config updates.

- **`eurekaserver`**
  - Service registry/discovery server.
  - Keeps track of healthy service instances.
  - Enables dynamic service lookup by logical service name.

- **`apigateway`**
  - Client-facing entry point.
  - Routes incoming requests to downstream microservices.
  - Uses service discovery (`lb://service-name`) to resolve targets.

- **`quizservice`**
  - Owns quiz domain operations (create/get quizzes).
  - Aggregates quiz data with question data by calling `questionservice`.

- **`questionservice`**
  - Owns question domain operations (create/get questions).
  - Provides questions by quiz ID for composition by `quizservice`.

---

## 2) Architecture Details

### 2.1 Interaction Flow

1. A service starts and reads bootstrap/application config.
2. The service contacts **Config Server** using `spring.config.import=optional:configserver:http://localhost:8084`.
3. The service receives environment-specific properties from the Git config repo.
4. The service starts and registers itself with **Eureka Server**.
5. The **API Gateway** receives client traffic and resolves destination services through Eureka.
6. `quizservice` may call `questionservice` through OpenFeign using the logical service name `questionservice`.

### 2.2 Runtime Request Flow (Typical)

- **Client -> API Gateway -> quizservice**
- `quizservice` retrieves quiz entities from its DB.
- For each quiz (or a specific quiz), `quizservice` invokes `questionservice` (`/question/quiz/{quizId}`) using Feign.
- Response is composed and sent back through gateway to the client.

### 2.3 Communication Patterns

- **Synchronous communication**
  - HTTP/REST between clients and gateway.
  - HTTP/REST between `quizservice` and `questionservice` via OpenFeign.
- **Service discovery-based routing**
  - Logical names resolved through Eureka.
- **Asynchronous communication**
  - Not implemented in current codebase (no messaging broker dependencies present).

---

## 3) Service-Level Documentation

## 3.1 `configserver`

### Description
Centralized configuration server enabled by `@EnableConfigServer`.

### Key dependencies
- `org.springframework.cloud:spring-cloud-config-server`
- `org.springframework.boot:spring-boot-starter-test` (test scope)

### Configuration details
- Runs on port `8084` (from local `application.properties`).
- Uses a Git repo as config source.
- `spring.cloud.config.server.git.clone-on-start=true` clones config on startup.

## 3.2 `eurekaserver`

### Description
Discovery server enabled by `@EnableEurekaServer`.

### Key dependencies
- `org.springframework.boot:spring-boot-starter-webmvc`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-server`
- `org.springframework.cloud:spring-cloud-starter-config`
- `org.springframework.boot:spring-boot-starter-test` (test scope)

### Configuration details
- Imports external config from Config Server.
- Typical Eureka server properties (port/register/fetch) are shown in local properties as comments and usually supplied via Config Server.

## 3.3 `apigateway`

### Description
Gateway service providing centralized routing into the microservice ecosystem.

### Key dependencies
- `org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`
- `org.springframework.cloud:spring-cloud-starter-config`
- `org.springframework.boot:spring-boot-starter-test` (test scope)

### Configuration details
- Imports external config from Config Server.
- Route examples exist in local properties (commented) using `spring.cloud.gateway.server.webmvc.routes[*]`.
- Expected to use load-balanced URIs such as `lb://quizservice` and `lb://questionservice`.

## 3.4 `questionservice`

### Description
Question domain microservice exposing CRUD-like APIs and query by quiz ID.

### Key dependencies
- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.springframework.boot:spring-boot-starter-webmvc`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`
- `org.springframework.cloud:spring-cloud-starter-config`
- `com.mysql:mysql-connector-j` (runtime)
- `org.projectlombok:lombok` (optional)
- `org.springframework.boot:spring-boot-starter-test` (test scope)

### Configuration details
- JPA entity `Question` with repository `QuestionRepository`.
- Registers with Eureka as `questionservice`.
- Uses MySQL for persistence.
- Imports config from Config Server.

## 3.5 `quizservice`

### Description
Quiz domain microservice that stores quizzes and enriches responses with questions from `questionservice`.

### Key dependencies
- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.springframework.boot:spring-boot-starter-webmvc`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`
- `org.springframework.cloud:spring-cloud-starter-config`
- `org.springframework.cloud:spring-cloud-starter-openfeign`
- `com.mysql:mysql-connector-j` (runtime)
- `org.projectlombok:lombok` (optional)
- `org.springframework.boot:spring-boot-starter-test` (test scope)

### Configuration details
- Feign client interface `QuestionClient` with `@FeignClient(name = "questionservice")`.
- `@EnableFeignClients` enabled in main application class.
- MySQL-backed JPA for `Quiz` entity.
- Imports config from Config Server.

---

## 4) Application Properties Reference

> Note: Several local property values are commented in this repository and appear intended to be centrally managed through Config Server + Git config repo.

## 4.1 `apigateway/src/main/resources/application.properties`

- `spring.application.name=apigateway`
  - Logical application name. Used for config lookup and service identity.
- `spring.config.import=optional:configserver:http://localhost:8084`
  - Imports external configuration from Config Server.
- Commented route properties:
  - `spring.cloud.gateway.server.webmvc.routes[0/1].id`
  - `spring.cloud.gateway.server.webmvc.routes[0/1].uri`
  - `spring.cloud.gateway.server.webmvc.routes[0/1].predicates[0]`
  - These define gateway route IDs, destination URIs (typically `lb://...`), and path predicates.
- Commented `eureka.client.service-url.defaultZone`
  - Eureka endpoint used by gateway for service discovery.

## 4.2 `configserver/src/main/resources/application.properties`

- `spring.application.name=configserver`
  - Config server app name.
- `server.port=8084`
  - Dedicated port for config service.
- `spring.cloud.config.server.git.uri=...`
  - Git repository URL where centralized config files live.
- `spring.cloud.config.server.git.clone-on-start=true`
  - Clones config repository during startup for early validation.
- `spring.cloud.config.server.git.skipSslValidation=true`
  - Disables SSL cert validation for Git fetch (use carefully; avoid in strict production setups).

## 4.3 `eurekaserver/src/main/resources/application.properties`

- `spring.application.name=eurekaserver`
  - App name for config and discovery identity.
- `spring.config.import=optional:configserver:http://localhost:8084`
  - Pulls externalized config.
- Commented `server.port=8761`
  - Standard Eureka server port.
- Commented `eureka.client.register-with-eureka=false`
  - Server should not register itself as a client in most setups.
- Commented `eureka.client.fetch-registry=false`
  - Server typically does not fetch registry as a client.

## 4.4 `questionservice/src/main/resources/application.properties`

- `spring.application.name=questionservice`
  - Service identity used in Eureka and Config Server.
- `spring.config.import=optional:configserver:http://localhost:8084`
  - Imports centralized configuration.
- `spring.datasource.password=...`
  - DB password (should be externalized/secured).
- Commented properties include:
  - `server.port`
  - `spring.datasource.url`
  - `spring.datasource.username`
  - `spring.jpa.hibernate.ddl-auto`
  - `spring.jpa.show-sql`
  - `spring.jpa.properties.hibernate.format_sql`
  - `eureka.client.service-url.defaultZone`
  - These indicate expected DB/JPA and discovery settings, likely moved to Config Server repo.

## 4.5 `quizservice/src/main/resources/application.properties`

- `spring.application.name=quizservice`
  - Service identity.
- `spring.config.import=optional:configserver:http://localhost:8084`
  - Imports centralized configuration.
- `spring.datasource.password=...`
  - DB password (should be externalized/secured).
- Commented properties mirror `questionservice`:
  - server port
  - datasource URL/username
  - JPA options
  - Eureka default zone

---

## 5) Dependencies (Major)

## 5.1 Shared Platform Versions

- **Spring Boot parent**: `4.1.0`
- **Spring Cloud BOM**: `2025.1.2`
- **Java version**: `21`

## 5.2 By Service

- **`configserver`**
  - Config Server starter
  - Boot test starter

- **`eurekaserver`**
  - WebMVC starter
  - Eureka Server starter
  - Config Client starter
  - Boot test starter

- **`apigateway`**
  - Gateway Server WebMVC starter
  - Eureka Client starter
  - Config Client starter
  - Boot test starter

- **`questionservice`**
  - Data JPA starter
  - WebMVC starter
  - Eureka Client starter
  - Config Client starter
  - MySQL connector (runtime)
  - Lombok (optional)
  - Boot test starter

- **`quizservice`**
  - Data JPA starter
  - WebMVC starter
  - Eureka Client starter
  - Config Client starter
  - OpenFeign starter
  - MySQL connector (runtime)
  - Lombok (optional)
  - Boot test starter

---

## 6) Setup and Running Instructions

## 6.1 Prerequisites

- Java 21
- Maven wrapper usage (`mvnw.cmd` on Windows)
- MySQL accessible with credentials expected by active config
- Access to config Git repo used by Config Server

## 6.2 Recommended Startup Order

1. **Config Server** (`configserver`)
2. **Eureka Server** (`eurekaserver`)
3. **API Gateway** (`apigateway`)
4. **Microservices** (`questionservice`, `quizservice`)

### Why this order

- Services may fail or start with partial defaults if Config Server is unavailable.
- Services and gateway need Eureka available for registration/discovery.
- Gateway routes are most useful after backend services are registered.

## 6.3 Windows PowerShell Start Commands

```powershell
# Terminal 1
Set-Location C:\Users\2505420\Desktop\microservices\configserver
.\mvnw.cmd spring-boot:run
```

```powershell
# Terminal 2
Set-Location C:\Users\2505420\Desktop\microservices\eurekaserver
.\mvnw.cmd spring-boot:run
```

```powershell
# Terminal 3
Set-Location C:\Users\2505420\Desktop\microservices\apigateway
.\mvnw.cmd spring-boot:run
```

```powershell
# Terminal 4
Set-Location C:\Users\2505420\Desktop\microservices\questionservice
.\mvnw.cmd spring-boot:run
```

```powershell
# Terminal 5
Set-Location C:\Users\2505420\Desktop\microservices\quizservice
.\mvnw.cmd spring-boot:run
```

## 6.4 Basic Verification

- Check Eureka dashboard for registered instances (typically on port `8761` if configured that way).
- Call gateway routes for quiz/question APIs.
- Confirm that quiz responses include nested question data from Feign-based service-to-service calls.

---

## 7) Best Practices and Notes

## 7.1 Configuration Management

- Keep environment-specific configs in the external Git config repo, not hardcoded in service repos.
- Avoid committing plaintext secrets (DB passwords, tokens) to source control.
- Prefer secure secret sources (Vault/KMS/CI secret injection) for production.

## 7.2 Service Registration/Discovery

- Ensure unique `spring.application.name` per service.
- Keep health checks and Eureka lease settings tuned for target environment.
- Validate startup timing so services do not repeatedly fail when discovery/config is temporarily unavailable.

## 7.3 Gateway Routing

- Use explicit path predicates and avoid overly broad wildcard patterns.
- Prefer logical service URIs (`lb://service-name`) for resiliency.
- Add filters for cross-cutting concerns (auth, rate limiting, tracing) as system matures.

## 7.4 Inter-Service Communication

- Keep Feign contracts versioned and backward compatible.
- Add timeouts, retries, and circuit-breaking patterns where needed.
- Use DTOs and API versioning for stable service boundaries.

## 7.5 Observability and Reliability

- Add structured logging and correlation IDs across gateway and services.
- Expose health/readiness endpoints and monitor them.
- Introduce integration tests covering gateway -> service -> downstream service calls.

---

## 8) Quick Component Summary

- `configserver`: centralized properties from Git.
- `eurekaserver`: service registry for discovery.
- `apigateway`: single ingress and route dispatcher.
- `questionservice`: question data ownership.
- `quizservice`: quiz ownership + question aggregation via Feign.

---

## 9) Dependency and Property Matrix (Quick Reference)

### 9.1 Dependencies Table

| Service | Dependency/Property | Why used | Mandatory/Optional | Dev vs Prod recommendation |
|---|---|---|---|---|
| All services | `spring-boot-starter-parent:4.1.0` | Centralized Maven defaults and compatible dependency/plugin versions | Mandatory | Keep same across all modules in all environments |
| All services | `spring-cloud-dependencies:2025.1.2` (BOM) | Ensures Spring Cloud modules are version-aligned | Mandatory | Pin explicitly and upgrade in a controlled release cycle |
| All services | `java.version=21` | Compiler/runtime target | Mandatory | Keep aligned with runtime JDK image and CI |
| configserver | `org.springframework.cloud:spring-cloud-config-server` | Enables Config Server (`@EnableConfigServer`) | Mandatory | Required in both dev/prod |
| configserver | `org.springframework.boot:spring-boot-starter-test` | Spring test framework for context tests | Optional (runtime), Mandatory (for tests) | Keep in all environments for CI quality gates |
| eurekaserver | `org.springframework.boot:spring-boot-starter-webmvc` | Embedded HTTP stack for Eureka endpoints/dashboard | Mandatory | Keep in both dev/prod |
| eurekaserver | `org.springframework.cloud:spring-cloud-starter-netflix-eureka-server` | Service registry server capability | Mandatory | Required in both dev/prod |
| eurekaserver | `org.springframework.cloud:spring-cloud-starter-config` | Pulls config from Config Server | Optional (if fully local config), usually Mandatory | Prefer mandatory in prod for central config governance |
| eurekaserver | `org.springframework.boot:spring-boot-starter-test` | Test support | Optional (runtime), Mandatory (for tests) | Keep for CI pipelines |
| apigateway | `org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc` | Gateway routing/filtering and edge entry point | Mandatory | Required in both dev/prod |
| apigateway | `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client` | Service discovery for `lb://` routing | Optional (if static URIs), usually Mandatory | Mandatory in prod when instances scale dynamically |
| apigateway | `org.springframework.cloud:spring-cloud-starter-config` | Centralized gateway route/config loading | Optional (if local), usually Mandatory | Prefer centralized prod config |
| apigateway | `org.springframework.boot:spring-boot-starter-test` | Test support | Optional (runtime), Mandatory (for tests) | Keep for CI |
| questionservice | `org.springframework.boot:spring-boot-starter-data-jpa` | JPA repositories and ORM persistence | Mandatory | Required for DB-backed service |
| questionservice | `org.springframework.boot:spring-boot-starter-webmvc` | REST controllers and HTTP API | Mandatory | Required in both dev/prod |
| questionservice | `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client` | Registers/discovers via Eureka | Optional (standalone), usually Mandatory | Mandatory in prod microservice topology |
| questionservice | `org.springframework.cloud:spring-cloud-starter-config` | Externalized service config | Optional (local), usually Mandatory | Strongly recommended in prod |
| questionservice | `com.mysql:mysql-connector-j` (runtime) | JDBC driver for MySQL | Mandatory (with MySQL) | Keep runtime scope; match DB version in prod |
| questionservice | `org.projectlombok:lombok` | Reduces boilerplate (`@Getter`, `@Setter`, constructors) | Optional | Allowed in dev/prod builds; keep annotation processing consistent in CI |
| questionservice | `org.springframework.boot:spring-boot-starter-test` | Test support | Optional (runtime), Mandatory (for tests) | Keep for CI |
| quizservice | `org.springframework.boot:spring-boot-starter-data-jpa` | ORM + repository support for quiz data | Mandatory | Required for DB-backed service |
| quizservice | `org.springframework.boot:spring-boot-starter-webmvc` | REST API endpoints | Mandatory | Required in both dev/prod |
| quizservice | `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client` | Discovery registration and lookup | Optional (standalone), usually Mandatory | Mandatory in prod distributed setup |
| quizservice | `org.springframework.cloud:spring-cloud-starter-config` | Centralized configuration import | Optional (local), usually Mandatory | Recommended in prod |
| quizservice | `org.springframework.cloud:spring-cloud-starter-openfeign` | Declarative HTTP client to call `questionservice` | Mandatory (for current design) | Keep; add timeout/retry/circuit-breaker for prod resiliency |
| quizservice | `com.mysql:mysql-connector-j` (runtime) | MySQL JDBC connectivity | Mandatory (with MySQL) | Keep runtime scope; test compatibility with prod DB |
| quizservice | `org.projectlombok:lombok` | Boilerplate reduction | Optional | Keep if team standardizes Lombok usage |
| quizservice | `org.springframework.boot:spring-boot-starter-test` | Test support | Optional (runtime), Mandatory (for tests) | Keep in CI |

### 9.2 Application Properties Table

| Service | Dependency/Property | Why used | Mandatory/Optional | Dev vs Prod recommendation |
|---|---|---|---|---|
| apigateway | `spring.application.name=apigateway` | Service identity for Config Server and Eureka | Mandatory | Keep stable; avoid renaming after clients/routes depend on it |
| apigateway | `spring.config.import=optional:configserver:http://localhost:8084` | Imports central config at startup | Optional (`optional:`), usually Mandatory | Prefer non-optional/fail-fast in prod once infra is stable |
| apigateway | `server.port` (commented) | Gateway listen port | Mandatory (effective value required somewhere) | Keep explicit per environment to avoid port conflicts |
| apigateway | `eureka.client.service-url.defaultZone` (commented) | Eureka endpoint for registration/discovery | Mandatory in discovery-based topology | Externalize per environment; support HA Eureka endpoints in prod |
| apigateway | `spring.cloud.gateway.server.webmvc.routes[*].id` (commented) | Unique route identifier | Mandatory for each route definition | Use clear naming convention (domain + version) |
| apigateway | `spring.cloud.gateway.server.webmvc.routes[*].uri` (commented) | Route destination (`lb://service`) | Mandatory for each route | Prefer `lb://` for dynamic discovery in prod |
| apigateway | `spring.cloud.gateway.server.webmvc.routes[*].predicates[*]` (commented) | Match incoming paths to route | Mandatory for each route | Keep path patterns explicit to avoid accidental route capture |
| configserver | `spring.application.name=configserver` | Config server identity | Mandatory | Keep stable across environments |
| configserver | `server.port=8084` | Config Server HTTP port | Mandatory | Keep configurable via env var/profile in prod |
| configserver | `spring.cloud.config.server.git.uri=...` | Source Git repository for all external configs | Mandatory | Use secured/private repo with least-privilege access |
| configserver | `spring.cloud.config.server.git.clone-on-start=true` | Early validation that repo is reachable and valid | Optional | Useful in dev; in prod keep enabled for fail-fast startup validation |
| configserver | `spring.cloud.config.server.git.skipSslValidation=true` | Bypasses SSL cert validation for Git remote | Optional | Avoid in prod; install trusted CA/cert chain instead |
| eurekaserver | `spring.application.name=eurekaserver` | Discovery service name and config lookup key | Mandatory | Keep constant to avoid client misconfiguration |
| eurekaserver | `spring.config.import=optional:configserver:http://localhost:8084` | Pulls central configuration | Optional (`optional:`), usually Mandatory | Prefer fail-fast config in prod once platform is stable |
| eurekaserver | `server.port=8761` (commented) | Standard Eureka dashboard/API port | Mandatory (effective value required somewhere) | Keep explicit and protected by network policy in prod |
| eurekaserver | `eureka.client.register-with-eureka=false` (commented) | Prevents server self-registration as client | Recommended | Keep `false` for standalone server role |
| eurekaserver | `eureka.client.fetch-registry=false` (commented) | Prevents server fetching registry as client | Recommended | Keep `false` unless special clustered setup requires otherwise |
| questionservice | `spring.application.name=questionservice` | Service registration name and config key | Mandatory | Keep stable; Feign/gateway depend on it |
| questionservice | `spring.config.import=optional:configserver:http://localhost:8084` | Loads central config | Optional (`optional:`), usually Mandatory | Prefer central config in prod; use profile overrides for local dev |
| questionservice | `server.port` (commented) | Service HTTP port | Mandatory (effective value required somewhere) | Use profile/env-specific ports in dev; orchestrator-managed in prod |
| questionservice | `spring.datasource.url` (commented) | Database JDBC endpoint | Mandatory for DB usage | Externalize per environment; avoid hardcoding host/password |
| questionservice | `spring.datasource.username` (commented) | DB username | Mandatory for DB auth | Use secret manager or env injection in prod |
| questionservice | `spring.datasource.password=...` | DB password | Mandatory for DB auth | Do not commit plaintext; move to secret store immediately |
| questionservice | `spring.jpa.hibernate.ddl-auto` (commented) | Schema lifecycle strategy | Optional but critical | `update` for local dev convenience; use `validate`/migrations in prod |
| questionservice | `spring.jpa.show-sql` (commented) | Logs SQL statements | Optional | Enable in dev troubleshooting; disable in prod for noise/security |
| questionservice | `spring.jpa.properties.hibernate.format_sql` (commented) | Formats SQL logs for readability | Optional | Dev-only usually |
| questionservice | `eureka.client.service-url.defaultZone` (commented) | Discovery server URL | Mandatory in Eureka topology | Use multiple zones/URLs for resilient prod setup |
| quizservice | `spring.application.name=quizservice` | Service identity and discovery name | Mandatory | Keep stable for routing and Feign resolution |
| quizservice | `spring.config.import=optional:configserver:http://localhost:8084` | Imports central config | Optional (`optional:`), usually Mandatory | Prefer centralized config + profile overlays |
| quizservice | `server.port` (commented) | Service listening port | Mandatory (effective value required somewhere) | Explicit in dev; environment-driven in prod |
| quizservice | `spring.datasource.url` (commented) | JDBC endpoint for quiz DB | Mandatory for DB usage | Externalize per environment |
| quizservice | `spring.datasource.username` (commented) | DB username | Mandatory for DB auth | Secret-managed credentials in prod |
| quizservice | `spring.datasource.password=...` | DB password | Mandatory for DB auth | Remove from repo; use secret source |
| quizservice | `spring.jpa.hibernate.ddl-auto` (commented) | ORM schema management strategy | Optional but critical | Dev: `update`; Prod: migrations + `validate` |
| quizservice | `spring.jpa.show-sql` (commented) | SQL logging | Optional | Dev only |
| quizservice | `spring.jpa.properties.hibernate.format_sql` (commented) | SQL log formatting | Optional | Dev only |
| quizservice | `eureka.client.service-url.defaultZone` (commented) | Eureka endpoint | Mandatory in discovery-based deployment | Externalize and support HA endpoints in prod |

