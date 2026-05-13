# Workout Tracker API

A production-ready REST API built with Java and Spring Boot for managing workout exercises and fitness routines.

This project was developed as part of the Advanced Java & DevOps mini-project and demonstrates backend development best practices including CRUD operations, validation, Docker, CI/CD, testing, and cloud deployment.

---

# Features

* Create, update, retrieve, and delete workouts
* Input validation with Jakarta Validation
* Search workouts by keyword
* Filter workouts by muscle group and difficulty
* Pagination and sorting support
* Global exception handling
* Integration testing with H2 database
* Dockerized application
* CI/CD pipeline with GitHub Actions
* PostgreSQL production deployment on Render
* Swagger/OpenAPI documentation

---

# Technologies Used

* Java 17
* Spring Boot 4
* Spring Web MVC
* Spring Data JPA
* MySQL
* PostgreSQL
* H2 Database
* Docker
* Docker Compose
* GitHub Actions
* Maven
* Swagger / OpenAPI

---

# Project Structure

```text
src
 ├── main
 │    ├── java/com/ironhack/workouttracker
 │    │     ├── controller
 │    │     ├── service
 │    │     ├── repository
 │    │     ├── model
 │    │     ├── dto
 │    │     ├── exception
 │    │     └── config
 │    └── resources
 │          ├── application.properties
 │          ├── application-test.properties
 │          └── application-prod.properties
 │
 └── test
      └── java/com/ironhack/workouttracker
```

---

# Entity

## Workout

| Field           | Type          | Description         |
| --------------- | ------------- | ------------------- |
| id              | Long          | Workout ID          |
| name            | String        | Workout name        |
| description     | String        | Workout description |
| muscleGroup     | Enum          | Target muscle group |
| difficulty      | Enum          | Workout difficulty  |
| durationMinutes | Integer       | Estimated duration  |
| equipment       | String        | Required equipment  |
| createdAt       | LocalDateTime | Creation timestamp  |

---

# Validation Rules

* name cannot be blank
* name length must be between 3 and 100 characters
* description cannot be blank
* durationMinutes must be between 1 and 300

Example:

```java
@NotBlank
@Size(min = 3, max = 100)
private String name;
```

---

# API Endpoints

Base URL:

```text
/api/workouts
```

---

## Create Workout

### Request

```http
POST /api/workouts
Content-Type: application/json
```

### Body

```json
{
  "name": "Push Ups",
  "description": "Chest bodyweight exercise",
  "muscleGroup": "CHEST",
  "difficulty": "BEGINNER",
  "durationMinutes": 15,
  "equipment": "None"
}
```

### Response

```json
{
  "id": 1,
  "name": "Push Ups",
  "description": "Chest bodyweight exercise",
  "muscleGroup": "CHEST",
  "difficulty": "BEGINNER",
  "durationMinutes": 15,
  "equipment": "None",
  "createdAt": "2026-05-12T14:00:00"
}
```

---

## Get Workout By ID

```http
GET /api/workouts/{id}
```

Example:

```http
GET /api/workouts/1
```

---

## Update Workout

```http
PUT /api/workouts/{id}
```

### Body

```json
{
  "name": "Advanced Push Ups",
  "description": "Advanced chest exercise",
  "muscleGroup": "CHEST",
  "difficulty": "ADVANCED",
  "durationMinutes": 25,
  "equipment": "None"
}
```

---

## Delete Workout

```http
DELETE /api/workouts/{id}
```

Response:

```text
204 No Content
```

---

# Bonus Endpoints

## Get All Workouts

```http
GET /api/workouts
```

---

## Filter By Muscle Group

```http
GET /api/workouts?muscleGroup=CHEST
```

---

## Filter By Difficulty

```http
GET /api/workouts?difficulty=BEGINNER
```

---

## Search Workouts

```http
GET /api/workouts/search?keyword=push
```

---

## Pagination

```http
GET /api/workouts?page=0&size=5&sort=createdAt,desc
```

---

# Swagger Documentation

Swagger UI is available at:

```text
/swagger-ui.html
```

OpenAPI automatically generates interactive API documentation for testing endpoints directly from the browser.

---

# Profiles

## Default Profile (Local Development)

Uses MySQL database.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/workout_tracker
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

---

## Test Profile

Uses H2 in-memory database.

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

---

## Production Profile

Uses PostgreSQL database on Render.

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
server.port=${PORT:8080}
```

---

# Local Setup Instructions

## 1. Clone the repository

```bash
git clone https://github.com/your-username/workout-tracker-api.git
```

---

## 2. Navigate to the project

```bash
cd workout-tracker-api
```

---

## 3. Configure MySQL

Create a database named:

```text
workout_tracker
```

Update credentials in:

```text
src/main/resources/application.properties
```

---

## 4. Run the application

```bash
./mvnw spring-boot:run
```

Application runs at:

```text
http://localhost:8080
```

---

# Running Tests

```bash
./mvnw test
```

Integration tests use the H2 in-memory database.

---

# Docker Setup

## Build and run containers

```bash
docker compose up --build
```

This starts:

* Spring Boot application
* MySQL database

---

# CI/CD Pipeline

GitHub Actions pipeline automatically:

* Builds the project
* Runs tests
* Verifies Maven build

Pipeline triggers on:

* Push to main
* Pull requests

Workflow file:

```text
.github/workflows/ci.yml
```
---

```md
# Project Links

|  Resource  | Link |
|------------|----------------------------------------------------------------------------------------------------------|
| Jira Board | [Open Jira](https://yusif2006xankisiyev.atlassian.net/jira/software/projects/KAN/boards/2)               |
| Live API | [Render Deployment](https://mini-project-spring-boot-rest-api.onrender.com/)                               |
| Live API Test Request | [Render Deployment Test](https://mini-project-spring-boot-rest-api.onrender.com/api/workouts) |

---
# Deployment

The application is deployed on Render using:

* Docker
* PostgreSQL database
* Production Spring profile

Environment variables:

```text
DATABASE_URL
SPRING_PROFILES_ACTIVE=prod
```

---

# Future Improvements

* Authentication with Spring Security
* User accounts
* Favorite workouts
* Workout plans
* Exercise images
* External fitness API integration
* Mobile frontend integration

---

# Author

Developed by Yusif as part of the Advanced Java & DevOps course.
