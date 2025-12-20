# Performance Backend

This project is a Spring Boot application acting as a System Under Test for performance testing. It simulates a user management system with orders.

## Prerequisites

- Java 17+
- Maven (wrapper included)
- Docker & Docker Compose (optional, for external Postgres)

## Running the Application

### Option 1: Docker Database + Local Spring Boot (Recommended)
This starts the Postgres database in Docker and the application locally.

**Commands in sequence:**

1. Start the database:
```bash
docker-compose up -d
```

2. Start the application (open a new terminal):
```bash
./mvnw spring-boot:run -DskipTests
```

3. Verify the application:
```bash
curl -v http://localhost:8081/api/users/1
```

### Option 2: Local with H2 (In-Memory DB)
Useful for quick testing without external dependencies. The application will use an in-memory H2 database.

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:h2:mem:sut_db --spring.datasource.driver-class-name=org.h2.Driver --spring.jpa.database-platform=org.hibernate.dialect.H2Dialect --spring.datasource.username=sa --spring.datasource.password="
```

## Verification

The application comes with a `DataSeeder` that populates the database with 50 users on startup.

### Verify User API
To verify the application is running and the data is seeded, verify the following endpoint:

**Endpoint:** `GET /api/users/1`

**Command:**
```bash
curl -v http://localhost:8081/api/users/1
```

**Expected Response:**
```json
{
  "id": 1,
  "name": "...",
  "email": "...",
  "createdAt": "..."
}
```

## Monitoring / Actuator
The application exposes Actuator endpoints for monitoring:
- Health: `http://localhost:8081/actuator/health`
- Prometheus Metrics: `http://localhost:8081/actuator/prometheus`
