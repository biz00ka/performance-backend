# Performance Backend

This project is a Spring Boot application acting as a System Under Test for performance testing. It simulates a user management system with orders.

## Prerequisites

- Java 17+
- Maven (wrapper included)
- Docker & Docker Compose (optional, for external Postgres)

## Running the Application

### Option 1: Full Stack in Docker (Recommended for Demo)
This starts the App, Postgres, and Prometheus UI. This is exactly how it will run on AWS.

```bash
docker-compose up -d --build
```
- **App API**: `http://localhost:8081`
- **Prometheus UI**: `http://localhost:9090`
- **Postgres**: `localhost:5432`

### Option 2: Database Only + Local Spring Boot (Recommended for Development)
If you want to run the application in your IDE or via terminal while developing.

1. Start only the database:
   ```bash
   docker-compose up -d db
   ```

2. Start the application locally:
   ```bash
   ./mvnw spring-boot:run -DskipTests
   ```

3. (Optional) Start Prometheus to monitor your local app:
   ```bash
   docker-compose up -d prometheus
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

### User Management (CRUD)

Follow these commands to manage custom users:

**Create a new user:**
```bash
curl -X POST http://localhost:8081/api/users \
     -H "Content-Type: application/json" \
     -d '{"name": "John Doe", "email": "john@example.com"}'
```

**Update an existing user:**
```bash
curl -X PUT http://localhost:8081/api/users/1 \
     -H "Content-Type: application/json" \
     -d '{"name": "John Updated", "email": "john.updated@example.com"}'
```

**Delete a user:**
```bash
curl -X DELETE http://localhost:8081/api/users/1
```

## Stopping the Application

### Graceful Shutdown (Docker Compose)

To stop the entire stack (App, DB, Prometheus) and release all ports (`8081`, `5432`, `9090`):

```bash
docker-compose down
```

> [!NOTE]
> This will stop and remove the containers but **preserve your data** in the `postgres_data` volume. If you want to completely wipe everything including data, use `docker-compose down -v`.

### Local Process Cleanup (Manual)

If you ran the app locally (Option 2) and need to kill the process manually:
```bash
lsof -ti:8081 | xargs kill -9
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
