# Observability Guide: Performance Backend

This guide explains how to monitor your System Under Test (SUT) using Prometheus metrics.

## How to Access Metrics

When the service is running, Prometheus metrics are available at:
`http://[HOST]:8081/actuator/prometheus`

---

## Prometheus UI

You can access the Prometheus web interface to query and visualize metrics directly:
`http://[HOST]:9090`

---

## Key Metrics for CRUD Operations

Spring Boot Actuator (via Micrometer) automatically generates high-precision metrics for every HTTP request. Use these metrics to observe your CRUD operations.

### 1. Request Rate and Latency
The primary metric is `http_server_requests_seconds_count` (total requests) and `http_server_requests_seconds_sum` (total time).

#### Examples:
- **Total Create User Requests**:
  `http_server_requests_seconds_count{uri="/api/users", method="POST"}`
- **Average Latency for Get User**:
  `rate(http_server_requests_seconds_sum{uri="/api/users/{id}", method="GET"}[5m]) / rate(http_server_requests_seconds_count{uri="/api/users/{id}", method="GET"}[5m])`

### 2. Status Codes (Success vs Failure)
Use the `status` tag to monitor reliability.
- **Success Rate**: Look for `status="200"` or `status="201"`.
- **Error Rate**: Look for `status="4xx"` or `status="5xx"`.

### 3. Database Connection Pool
Since performance testing often stresses the database, monitor the Hikari connection pool:
- `hikaricp_connections_active`: Connections currently in use.
- `hikaricp_connections_pending`: Requests waiting for a connection.

---

## How to use during a Demo

1.  **Baseline**: Start the service and check the `/actuator/prometheus` endpoint.
2.  **Generate Load**: Use `curl` or a tool like `ab` or `jmeter` to perform CRUD operations.
3.  **Observe**: Refresh the metrics endpoint. You will see the counts and sums increase, reflecting the real-time performance of your SUT.

> [!TIP]
> For a more visual demo, you can run a local **Grafana** instance and connect it to your service's Prometheus endpoint.
