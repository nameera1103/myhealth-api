# MyHealth API

A RESTful backend service for managing personal health records, built with **Spring Boot**. Users can create accounts, log in securely, and manage their health records (weight, temperature, blood pressure, and notes) through a clean REST API.

This project was built to practice production-style backend development: layered architecture, JPA persistence, input validation, secure password handling, and proper HTTP semantics.

---

## Features

- **User accounts** — signup and login with secure authentication
- **Password security** — passwords hashed with BCrypt, never stored in plain text
- **Health record CRUD** — create, read, update, and delete personal health records
- **Input validation** — field-level rules with clear, structured error responses
- **Proper HTTP status codes** — `200`, `400`, `401`, `404` used correctly
- **Global exception handling** — consistent JSON error responses across the API

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| Data access | Spring Data JPA (Hibernate) |
| Database | H2 (in-memory, for development) |
| Security | Spring Security (BCrypt password hashing) |
| Validation | Jakarta Bean Validation |
| Build tool | Maven |

---

## Architecture

The project follows a layered architecture that separates concerns:

```
Controller  ->  receives HTTP requests, returns responses
Repository  ->  data access (Spring Data JPA generates the SQL)
Entity      ->  maps Java objects to database tables
Config      ->  security and application configuration
```

- **Controllers** handle incoming requests and delegate work — they contain no SQL.
- **Repositories** extend `JpaRepository`, so standard database operations are generated automatically. Custom finders (e.g. `findByUsername`) are derived from method names.
- **Entities** are annotated POJOs that Hibernate maps to tables.
- A **global exception handler** (`@RestControllerAdvice`) converts validation failures into clean JSON error responses.

---

## API Endpoints

### Users

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users/signup` | Create a new account |
| `POST` | `/api/users/login` | Authenticate and log in |

### Health Records

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/records` | Get all records |
| `GET` | `/api/records/{id}` | Get a single record |
| `POST` | `/api/records` | Create a record |
| `PUT` | `/api/records/{id}` | Update a record |
| `DELETE` | `/api/records/{id}` | Delete a record |

### Example: create a health record

```http
POST /api/records
Content-Type: application/json

{
    "weight": "70",
    "temperature": "36.6",
    "bloodPressure": "120/80",
    "note": "Feeling good today",
    "date": "2026-06-29"
}
```

### Example: sign up

```http
POST /api/users/signup
Content-Type: application/json

{
    "username": "namira",
    "password": "Test@123",
    "firstName": "Namira",
    "lastName": "Mulla"
}
```

---

## Validation Rules

**Health records**
- At least one field must be provided
- Weight and temperature must be numeric
- Blood pressure must follow the format `120/80`
- Notes must be under 250 characters and fewer than 50 words

**Passwords**
- At least 8 characters
- At least one uppercase letter, one number, and one special character

Invalid input returns a `400 Bad Request` with a JSON message describing the problem.

---

## Running Locally

### Prerequisites
- Java 21 or later
- Maven (or use the included Maven wrapper `./mvnw`)

### Steps

```bash
# Clone the repository
git clone https://github.com/nameera1103/myhealth-api.git
cd myhealth-api

# Run the application
./mvnw spring-boot:run
```

The API will start at `http://localhost:8080`.

### Database console (development)

While the app is running, the H2 web console is available at:

```
http://localhost:8080/h2-console
```

Connect using JDBC URL `jdbc:h2:mem:myhealthdb`, user `sa`, and no password.

> Note: the in-memory database resets every time the application restarts.

---

## Testing the API

All endpoints can be tested with [Postman](https://www.postman.com/) or `curl`. Example:

```bash
curl -X POST http://localhost:8080/api/records \
  -H "Content-Type: application/json" \
  -d '{"weight":"70","temperature":"36.6","bloodPressure":"120/80","note":"feeling good","date":"2026-06-29"}'
```

---

## Roadmap

Planned improvements:

- [ ] Migrate from H2 to PostgreSQL for persistent storage
- [ ] Token-based authentication (JWT) to protect record endpoints per user
- [ ] Link health records to their owning user
- [ ] Deploy to a cloud platform
- [ ] Automated tests (unit and integration)

---

## About

Built as a learning project to develop production-style backend skills with Spring Boot, focusing on understanding each layer rather than just wiring it together.
