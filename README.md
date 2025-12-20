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

1. Navigate to the project directory:
```bash
cd performance-backend
```

2. Start the database:
```bash
docker-compose up -d
```

3. Start the application (open a new terminal):
```bash
./mvnw spring-boot:run -DskipTests
```

4. Verify the application:
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

## Stopping the Application

### Graceful Shutdown

1. **Spring Boot App**: If running in a terminal, press `Ctrl+C` to stop it gracefully. **This will release port 8081.**

2. **Database (Docker)**:
To stop and remove the database container (and release port 5432):
```bash
docker-compose down
```

## Monitoring / Actuator
The application exposes Actuator endpoints for monitoring:
- Health: `http://localhost:8081/actuator/health`
- Prometheus Metrics: `http://localhost:8081/actuator/prometheus`

## Troubleshooting

### Port 8081 already in use

If you see an error that the port is already in use, you can find and kill the process occupying it:

1. Check what is using the port:
```bash
lsof -i :8081
```

2. Kill the process (replace `<PID>` with the Process ID from the previous command):
```bash
kill -9 <PID>
```

Or run this one-liner to kill it immediately:
```bash
lsof -ti:8081 | xargs kill -9
```
