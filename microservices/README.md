# Microservices Project Documentation

This project is a simple Spring Boot microservices system with service discovery, centralized config, API routing, and
two business services.

## 1) What is inside this project

- `eurekaserver` - Service registry (all services register here)
- `configserver` - Centralized configuration server (reads config from Git)
- `apigateway` - Single entry point, routes traffic to services
- `quizservice` - Quiz domain service
- `questionservice` - Question domain service

## 2) Tech stack

- Java 21
- Spring Boot `4.1.0`
- Spring Cloud `2025.1.2`
- Maven Wrapper (`mvnw.cmd`)
- MySQL (`newDb` database)
- Eureka, Config Server, Spring Cloud Gateway, OpenFeign (in quiz service)

## 3) How services talk to each other

1. Start `eurekaserver` on `8761`
2. Start `configserver` on `8084`
3. Start `questionservice` on `8082`
4. Start `quizservice` on `8081`
5. Start `apigateway` on `8083`

Request flow:

- Client -> API Gateway (`8083`)
- Gateway -> load-balanced route to `quizservice` or `questionservice` using Eureka names

## 4) All configuration files

## `eurekaserver/src/main/resources/application.properties`

```properties
spring.application.name=eurekaserver
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

Notes:

- Runs Eureka Server locally on port `8761`
- Server itself does not register as a client

## `configserver/src/main/resources/application.properties`

```properties
spring.application.name=configserver
server.port=8084
spring.cloud.config.server.git.skipSslValidation=true
spring.cloud.config.server.git.uri=https://github.com/architraghav-cts/microservice-config
spring.cloud.config.server.git.clone-on-start=true
```

Notes:

- Pulls external config from Git repository
- `skipSslValidation=true` is useful in local/dev when SSL trust is failing
- For production, use proper SSL truststore and set SSL validation back to secure mode

## `apigateway/src/main/resources/application.properties`

```properties
spring.application.name=apigateway
server.port=8083
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
spring.cloud.gateway.server.webmvc.routes[0].id=quizservice
spring.cloud.gateway.server.webmvc.routes[0].uri=lb://quizservice
spring.cloud.gateway.server.webmvc.routes[0].predicates[0]=Path=/quiz/**
spring.cloud.gateway.server.webmvc.routes[1].id=questionservice
spring.cloud.gateway.server.webmvc.routes[1].uri=lb://questionservice
spring.cloud.gateway.server.webmvc.routes[1].predicates[0]=Path=/question/**
```

Notes:

- `/quiz/**` -> `quizservice`
- `/question/**` -> `questionservice`
- Uses `lb://...` so service name must match Eureka registration

## `questionservice/src/main/resources/application.properties`

```properties
spring.application.name=questionservice
server.port=8082
spring.datasource.url=jdbc:mysql://localhost:3306/newDb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Notes:

- Uses MySQL database `newDb`
- JPA schema auto-update is enabled (`ddl-auto=update`)

## `quizservice/src/main/resources/application.properties`

```properties
spring.application.name=quizservice
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/newDb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=21186732Ar@
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.config.import=optional:configserver:http://localhost:8084
```

Notes:

- Uses Config Client import syntax correctly
- If Config Server is down, startup can continue because of `optional:`

## 5) Complete dependency list (all modules)

Common Maven setup in all modules:

- Parent: `org.springframework.boot:spring-boot-starter-parent:4.1.0`
- Java version: `21`
- Spring Cloud BOM: `org.springframework.cloud:spring-cloud-dependencies:2025.1.2`

### `eurekaserver/pom.xml`

- `org.springframework.boot:spring-boot-starter-webmvc`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-server`
- `org.springframework.boot:spring-boot-starter-webmvc-test` (test)

### `configserver/pom.xml`

- `org.springframework.cloud:spring-cloud-config-server`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`
- `org.springframework.boot:spring-boot-starter-test` (test)

### `apigateway/pom.xml`

- `org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`
- `org.springframework.boot:spring-boot-starter-test` (test)

### `questionservice/pom.xml`

- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.springframework.boot:spring-boot-starter-webmvc`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`
- `com.mysql:mysql-connector-j` (runtime)
- `org.projectlombok:lombok` (optional)
- `org.springframework.boot:spring-boot-starter-data-jpa-test` (test)
- `org.springframework.boot:spring-boot-starter-webmvc-test` (test)

### `quizservice/pom.xml`

- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.springframework.boot:spring-boot-starter-webmvc`
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`
- `com.mysql:mysql-connector-j` (runtime)
- `org.projectlombok:lombok` (optional)
- `org.springframework.cloud:spring-cloud-starter-openfeign`
- `org.springframework.cloud:spring-cloud-starter-config`
- `org.springframework.boot:spring-boot-starter-data-jpa-test` (test)
- `org.springframework.boot:spring-boot-starter-webmvc-test` (test)

## 6) Full workflow: quiz service, question service, and gateway routing

This is how the system works in simple steps.

### A) Service startup and registration flow

1. `eurekaserver` starts on `8761`.
2. `configserver` starts on `8084` and pulls config from Git.
3. `questionservice` starts on `8082`.
4. `quizservice` starts on `8081` with Feign enabled (`@EnableFeignClients`).
5. `apigateway` starts on `8083` and uses route rules from its `application.properties`.

### B) How `quizservice` calls `questionservice`

Code path used:

- Feign client: `quizservice/src/main/java/com/quiz/quizservice/service/QuestionClient.java`
    - `@FeignClient(name = "questionservice")`
    - `@GetMapping("/question/quiz/{quizId}")`
- Caller service: `quizservice/src/main/java/com/quiz/quizservice/service/QuizServiceImpl.java`
    - In `getAll()` and `getById()`, it calls `questionClient.getQuestionsOfQuiz(quiz.getId())`
- Target endpoint: `questionservice/src/main/java/com/question/questionservice/controller/QuestionController.java`
    - `@GetMapping("/quiz/{quizId}")` under `@RequestMapping("/question")`

Request chain for `GET /quiz/id/{id}`:

1. Client calls quiz API.
2. `QuizController` -> `QuizServiceImpl.getById(id)`.
3. Quiz data is loaded from `quizservice` database.
4. `QuizServiceImpl` calls Feign client method.
5. Feign resolves service name `questionservice` and calls `/question/quiz/{quizId}`.
6. `questionservice` returns question list.
7. `quizservice` attaches questions into the quiz response and returns final JSON.

### C) How API Gateway exposes one host/port for both services

Gateway file: `apigateway/src/main/resources/application.properties`

- `Path=/quiz/**` -> `lb://quizservice`
- `Path=/question/**` -> `lb://questionservice`

Meaning:

- Clients call only one base URL: `http://localhost:8083`
- Same gateway endpoint (host + port) serves both services using different path prefixes.

Examples:

- `http://localhost:8083/quiz/all` -> forwarded to `quizservice`
- `http://localhost:8083/question/all` -> forwarded to `questionservice`

### D) End-to-end example

1. Create questions in `questionservice` (through gateway):
    - `POST http://localhost:8083/question`
2. Create quiz in `quizservice` (through gateway):
    - `POST http://localhost:8083/quiz`
3. Fetch quiz details (through gateway):
    - `GET http://localhost:8083/quiz/id/{id}`
4. During step 3, `quizservice` internally calls `questionservice` and returns quiz + question list.

## 7) Run commands (Windows PowerShell)

From project root, run each service in a separate terminal:

```powershell
cd C:\Users\2505420\Desktop\microservices\eurekaserver
.\mvnw.cmd spring-boot:run
```

```powershell
cd C:\Users\2505420\Desktop\microservices\configserver
.\mvnw.cmd spring-boot:run
```

```powershell
cd C:\Users\2505420\Desktop\microservices\questionservice
.\mvnw.cmd spring-boot:run
```

```powershell
cd C:\Users\2505420\Desktop\microservices\quizservice
.\mvnw.cmd spring-boot:run
```

```powershell
cd C:\Users\2505420\Desktop\microservices\apigateway
.\mvnw.cmd spring-boot:run
```

## 8) Quick test URLs

- Eureka dashboard: `http://localhost:8761`
- Gateway route to quiz APIs: `http://localhost:8083/quiz/...`
- Gateway route to question APIs: `http://localhost:8083/question/...`

## 9) API contract (sample request/response)

All examples below use API Gateway base URL: `http://localhost:8083`

### A) Create question

- Endpoint: `POST /question`

Request JSON:

```json
{
  "question": "What is Spring Boot?",
  "quizId": 1
}
```

Sample response JSON:

```json
{
  "questionId": 101,
  "question": "What is Spring Boot?",
  "quizId": 1
}
```

### B) Get all questions

- Endpoint: `GET /question/all`

Sample response JSON:

```json
[
  {
    "questionId": 101,
    "question": "What is Spring Boot?",
    "quizId": 1
  },
  {
    "questionId": 102,
    "question": "What is Eureka Server used for?",
    "quizId": 1
  }
]
```

### C) Create quiz

- Endpoint: `POST /quiz`

Request JSON:

```json
{
  "title": "Spring Microservices Basics"
}
```

Sample response JSON:

```json
{
  "id": 1,
  "title": "Spring Microservices Basics",
  "questions": null
}
```

### D) Get quiz by id (with questions from questionservice)

- Endpoint: `GET /quiz/id/{id}`

Sample response JSON:

```json
{
  "id": 1,
  "title": "Spring Microservices Basics",
  "questions": [
    {
      "questionId": 101,
      "question": "What is Spring Boot?",
      "quizId": 1
    },
    {
      "questionId": 102,
      "question": "What is Eureka Server used for?",
      "quizId": 1
    }
  ]
}
```

### E) Get all quizzes (each quiz enriched with questions)

- Endpoint: `GET /quiz/all`

Sample response JSON:

```json
[
  {
    "id": 1,
    "title": "Spring Microservices Basics",
    "questions": [
      {
        "questionId": 101,
        "question": "What is Spring Boot?",
        "quizId": 1
      }
    ]
  }
]
```

### F) Tiny cURL/Postman quick test (1 minute)

Use this order to test quickly through API Gateway (`http://localhost:8083`).

#### cURL commands

```bash
curl -X POST "http://localhost:8083/question" -H "Content-Type: application/json" -d "{\"question\":\"What is Spring Boot?\",\"quizId\":1}"
curl -X POST "http://localhost:8083/question" -H "Content-Type: application/json" -d "{\"question\":\"What is Eureka Server used for?\",\"quizId\":1}"
curl -X POST "http://localhost:8083/quiz" -H "Content-Type: application/json" -d "{\"title\":\"Spring Microservices Basics\"}"
curl "http://localhost:8083/question/all"
curl "http://localhost:8083/quiz/id/1"
curl "http://localhost:8083/quiz/all"
```

#### Postman quick setup

- Create a collection with base URL variable `{{baseUrl}} = http://localhost:8083`
- Add request `POST {{baseUrl}}/question` with JSON body:

```json
{
  "question": "What is Spring Boot?",
  "quizId": 1
}
```

- Add request `POST {{baseUrl}}/quiz` with JSON body:

```json
{
  "title": "Spring Microservices Basics"
}
```

- Add request `GET {{baseUrl}}/quiz/id/1` to verify quiz + nested questions

## 10) Important security notes

- Database password is currently in plain text in properties files.
- Recommended: move credentials to Config Server repo, environment variables, or a secrets manager.
- `spring.cloud.config.server.git.skipSslValidation=true` is not recommended for production.

## 11) Dependency and property reference

### A) Unique dependencies and what they are used for

- `org.springframework.boot:spring-boot-starter-webmvc` - REST APIs and embedded web server.
- `org.springframework.boot:spring-boot-starter-data-jpa` - JPA repositories and Hibernate ORM.
- `com.mysql:mysql-connector-j` - MySQL JDBC driver.
- `org.projectlombok:lombok` - Boilerplate reduction for models/classes.
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-server` - Eureka service registry server.
- `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client` - Service registration and discovery.
- `org.springframework.cloud:spring-cloud-config-server` - Centralized config server backed by Git.
- `org.springframework.cloud:spring-cloud-starter-config` - Config client to fetch externalized config.
- `org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc` - API Gateway routing.
- `org.springframework.cloud:spring-cloud-starter-openfeign` - Declarative inter-service HTTP client (`@FeignClient`).
- `org.springframework.boot:spring-boot-starter-test` - General testing dependencies.
- `org.springframework.boot:spring-boot-starter-webmvc-test` - MVC/web layer test support.
- `org.springframework.boot:spring-boot-starter-data-jpa-test` - JPA/data layer test support.
- `org.springframework.cloud:spring-cloud-dependencies` (BOM) - Version alignment for Spring Cloud artifacts.

### B) Key `application.properties` keys and meaning

- `spring.application.name` - Logical service name used in logs, config, and Eureka.
- `server.port` - HTTP port for each service.
- `eureka.client.service-url.defaultZone` - Eureka server URL for client registration/discovery.
- `eureka.client.register-with-eureka` / `eureka.client.fetch-registry` - Disable client behavior on Eureka server.
- `spring.cloud.config.server.git.uri` - Git repository for centralized config.
- `spring.cloud.config.server.git.clone-on-start` - Clone config repo at startup.
- `spring.cloud.config.server.git.skipSslValidation` - Skip SSL verification for Git connection (dev only).
- `spring.config.import=optional:configserver:http://localhost:8084` - Config client import source.
- `spring.datasource.url` / `spring.datasource.username` / `spring.datasource.password` - Database connection settings.
- `spring.jpa.hibernate.ddl-auto` - Schema mode (`update` used here for dev convenience).
- `spring.jpa.show-sql` / `spring.jpa.properties.hibernate.format_sql` - SQL logging and formatting.
- `spring.cloud.gateway.server.webmvc.routes[n].id` - Gateway route identifier.
- `spring.cloud.gateway.server.webmvc.routes[n].uri=lb://<service-name>` - Load-balanced destination via Eureka.
- `spring.cloud.gateway.server.webmvc.routes[n].predicates[0]=Path=...` - URL path matching rule.

### C) Current project-specific notes

- `quizservice` is configured as a Config Client using `spring.config.import`.
- `quizservice` calls `questionservice` using OpenFeign (`QuestionClient`).
- Gateway exposes one base URL (`http://localhost:8083`) and routes by path prefix (`/quiz/**`, `/question/**`).

---

If you want, I can next add the same documentation in each service folder (`README.md`) with module-specific setup and
endpoint examples.

