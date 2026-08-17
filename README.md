# 🔗 URL Shortener API

A **RESTful URL Shortening Service** built with **Java, Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL**.

The application converts long URLs into short, unique codes and provides APIs to manage shortened URLs, redirect users to the original URLs, and track access statistics.

---

## 🚀 Features

* Create shortened URLs from long URLs
* Generate unique random short codes
* Retrieve shortened URL details
* Update an existing shortened URL
* Delete shortened URLs
* Redirect short URLs to original URLs
* Track URL access count
* Validate URLs using Jakarta Bean Validation
* Global exception handling
* PostgreSQL database persistence
* RESTful API architecture
* Layered application architecture
* DTO-based request and response handling

---

## 🛠️ Tech Stack

| Technology             | Purpose                  |
| ---------------------- | ------------------------ |
| **Java**               | Programming Language     |
| **Spring Boot**        | Backend Framework        |
| **Spring Web**         | REST API Development     |
| **Spring Data JPA**    | Database Access          |
| **Hibernate**          | ORM / Entity Persistence |
| **PostgreSQL**         | Relational Database      |
| **Jakarta Validation** | Request Validation       |
| **Maven**              | Dependency Management    |
| **Postman**            | API Testing              |

---

## 📁 Project Structure

```text
src/main/java/com/prajwal/urlshortener
│
├── controller
│   └── UrlController.java
│
├── dto
│   ├── UrlRequest.java
│   ├── UrlResponse.java
│   └── UrlStatsResponse.java
│
├── entity
│   └── Url.java
│
├── exception
│   ├── UrlNotFoundException.java
│   └── GlobalExceptionHandler.java
│
├── repository
│   └── UrlRepository.java
│
├── service
│   ├── UrlService.java
│   └── UrlServiceImpl.java
│
├── util
│   └── ShortCodeGenerator.java
│
└── UrlShortenerApplication.java
```

---

## 🏗️ Architecture

The application follows a **layered architecture** that separates API handling, business logic, database operations, and persistence.

```text
                    Client / Postman
                           │
                           ▼
                  ┌─────────────────┐
                  │   Controller    │
                  │  REST Endpoints │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │     Service     │
                  │ Business Logic  │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │   Repository    │
                  │   Spring Data  │
                  │       JPA       │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │   PostgreSQL    │
                  │    Database     │
                  └─────────────────┘
```

### Controller

Handles HTTP requests, request validation, path variables, and HTTP responses.

### Service

Contains the application's business logic, including:

* Short-code generation
* Short-code uniqueness checking
* URL creation
* URL retrieval
* URL updates
* URL deletion
* Access-count tracking
* URL redirection

### Repository

Uses **Spring Data JPA** to interact with the PostgreSQL database.

### Entity

Represents the `urls` table and maps Java objects to database records using JPA/Hibernate.

### DTO

Separates API request and response models from the database entity.

### Utility

Generates random short codes for shortened URLs.

### Exception

Provides centralized exception handling and meaningful error responses using a global exception handler.

---

# 🗄️ Database

The application uses **PostgreSQL** for persistent storage.

### Create Database

```sql
CREATE DATABASE url_shortener;
```

The `urls` table is automatically created and updated by Hibernate.

### URL Table

```text
urls
------------------------------------------------
id
original_url
short_code
created_at
updated_at
access_count
```

### Example Record

```text
id:            1
original_url:  https://www.google.com
short_code:    RC0mJ1
created_at:    2026-08-17T16:50:56
updated_at:    2026-08-17T16:50:56
access_count:  3
```

---

# ⚙️ Configuration

Update the following properties in:

```text
src/main/resources/application.properties
```

```properties
spring.application.name=url-shortener

spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

Replace `YOUR_PASSWORD` with your PostgreSQL password.

> **Note:** Do not commit real database passwords or other credentials to GitHub.

---

# 🔌 API Documentation

## 1. Create Short URL

Creates a new shortened URL and generates a unique short code.

### Request

```http
POST /shorten
```

Example:

```http
POST http://localhost:8080/shorten
```

### Request Body

```json
{
    "url": "https://www.google.com"
}
```

### Response

**201 Created**

```json
{
    "id": 1,
    "url": "https://www.google.com",
    "shortCode": "RC0mJ1",
    "createdAt": "2026-08-17T16:50:56.121588",
    "updatedAt": "2026-08-17T16:50:56.121588"
}
```

The generated `shortCode` is randomly created and checked for uniqueness before being stored.

---

## 2. Retrieve URL

Retrieves information about a shortened URL using its short code.

### Request

```http
GET /shorten/{shortCode}
```

Example:

```http
GET http://localhost:8080/shorten/RC0mJ1
```

### Response

**200 OK**

```json
{
    "id": 1,
    "url": "https://www.google.com",
    "shortCode": "RC0mJ1",
    "createdAt": "2026-08-17T16:50:56.121588",
    "updatedAt": "2026-08-17T16:50:56.121588"
}
```

### URL Not Found

**404 Not Found**

```json
{
    "status": 404,
    "message": "No URL found for short code: RC0mJ1",
    "timestamp": "2026-08-17T16:55:00"
}
```

---

## 3. Update Short URL

Updates the original URL associated with an existing short code.

### Request

```http
PUT /shorten/{shortCode}
```

Example:

```http
PUT http://localhost:8080/shorten/RC0mJ1
```

### Request Body

```json
{
    "url": "https://www.youtube.com"
}
```

### Response

**200 OK**

```json
{
    "id": 1,
    "url": "https://www.youtube.com",
    "shortCode": "RC0mJ1",
    "createdAt": "2026-08-17T16:50:56.121588",
    "updatedAt": "2026-08-17T17:10:20.121588"
}
```

The existing short code remains unchanged.

```text
Before:

RC0mJ1
   ↓
https://www.google.com


After Update:

RC0mJ1
   ↓
https://www.youtube.com
```

---

## 4. Delete Short URL

Deletes an existing shortened URL.

### Request

```http
DELETE /shorten/{shortCode}
```

Example:

```http
DELETE http://localhost:8080/shorten/RC0mJ1
```

### Response

```text
204 No Content
```

If the short code does not exist:

```text
404 Not Found
```

---

## 5. Get URL Statistics

Retrieves information about a shortened URL along with its access count.

### Request

```http
GET /shorten/{shortCode}/stats
```

Example:

```http
GET http://localhost:8080/shorten/RC0mJ1/stats
```

### Response

**200 OK**

```json
{
    "id": 1,
    "url": "https://www.google.com",
    "shortCode": "RC0mJ1",
    "createdAt": "2026-08-17T16:50:56.121588",
    "updatedAt": "2026-08-17T16:50:56.121588",
    "accessCount": 5
}
```

The `accessCount` represents the number of times the short URL has been used for redirection.

---

## 6. Redirect to Original URL

Redirects the user from the short URL to the original URL.

### Request

```http
GET /{shortCode}
```

Example:

```http
GET http://localhost:8080/RC0mJ1
```

### Redirect Flow

```text
RC0mJ1
   │
   ▼
Find URL in PostgreSQL
   │
   ▼
Increment accessCount
   │
   ▼
HTTP 301 Redirect
   │
   ▼
Original URL
```

Example:

```text
http://localhost:8080/RC0mJ1
              │
              ▼
https://www.google.com
```

---

# 🔄 URL Shortening Flow

```text
Client / Postman
      │
      │ POST /shorten
      │
      │ {"url":"https://www.google.com"}
      ▼
┌─────────────────┐
│  UrlController  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   UrlService    │
└────────┬────────┘
         │
         ├── Generate short code
         │
         ├── Check uniqueness
         │
         ├── Create Url entity
         │
         ▼
┌─────────────────┐
│  UrlRepository  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   PostgreSQL    │
└────────┬────────┘
         │
         ▼
    UrlResponse
         │
         ▼
       Client
```

---

# 🔀 Redirect Flow

```text
Browser
   │
   │ GET /RC0mJ1
   ▼
UrlController
   │
   ▼
UrlService
   │
   ├── Find URL
   │
   ├── Increment accessCount
   │
   ▼
PostgreSQL
   │
   ▼
HTTP 301 Redirect
   │
   ▼
Original URL
```

---

# 🧪 Testing with Postman

Recommended testing sequence:

```text
1. POST    /shorten
2. GET     /shorten/{shortCode}
3. PUT     /shorten/{shortCode}
4. GET     /shorten/{shortCode}/stats
5. GET     /{shortCode}
6. GET     /shorten/{shortCode}/stats
7. DELETE  /shorten/{shortCode}
8. GET     /shorten/{shortCode}
```

After calling the redirect endpoint, verify that the `accessCount` has increased.

---

# ▶️ How to Run

## 1. Clone the Repository

```bash
git clone <your-github-repository-url>
```

```bash
cd <project-folder>
```

## 2. Open the Project

You can open the project using:

* IntelliJ IDEA
* Eclipse
* VS Code

## 3. Configure PostgreSQL

Create the database:

```sql
CREATE DATABASE url_shortener;
```

Then update your database credentials in:

```text
src/main/resources/application.properties
```

## 4. Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or run:

```text
UrlShortenerApplication.java
```

## 5. Application URL

```text
http://localhost:8080
```

---

# 📌 HTTP Status Codes

| Status Code               | Meaning                        |
| ------------------------- | ------------------------------ |
| **200 OK**                | Request successfully processed |
| **201 Created**           | Short URL successfully created |
| **204 No Content**        | URL successfully deleted       |
| **301 Moved Permanently** | Redirect to original URL       |
| **400 Bad Request**       | Invalid URL or request         |
| **404 Not Found**         | Short URL does not exist       |

---

# 🔐 URL Validation

The API validates incoming URLs before processing them using **Jakarta Bean Validation**.

### Valid URL

```json
{
    "url": "https://www.google.com"
}
```

### Valid URL

```json
{
    "url": "http://example.com"
}
```

### Invalid URL

```json
{
    "url": ""
}
```

### Invalid URL

```json
{
    "url": "google.com"
}
```

Invalid requests return:

```text
400 Bad Request
```

---

# 🧠 Key Spring Boot Concepts Demonstrated

This project demonstrates practical usage of:

* RESTful API development
* `@RestController`
* `@PostMapping`
* `@GetMapping`
* `@PutMapping`
* `@DeleteMapping`
* `@RequestBody`
* `@PathVariable`
* `@Valid`
* Jakarta Bean Validation
* Dependency Injection
* Service Layer
* Repository Layer
* Spring Data JPA
* Hibernate ORM
* Entity Mapping
* PostgreSQL
* DTO Pattern
* Global Exception Handling
* `ResponseEntity`
* HTTP Status Codes
* HTTP 301 Redirection
* Transaction Management

---

# 🚀 Future Improvements

The current implementation focuses on the core URL-shortening functionality. Possible future enhancements include:

* [ ] Custom short-code support
* [ ] URL expiration
* [ ] User authentication and authorization
* [ ] Click history
* [ ] Detailed URL analytics
* [ ] IP, device, and browser statistics
* [ ] Rate limiting
* [ ] Redis caching
* [ ] Swagger/OpenAPI documentation
* [ ] Unit testing with JUnit and Mockito
* [ ] Integration testing
* [ ] Docker containerization
* [ ] CI/CD pipeline
* [ ] Cloud deployment

---

# 👨‍💻 Author

**Prajwal K**

Computer Science & Engineering Graduate

**Technologies:** Java • Spring Boot • REST API • Spring Data JPA • Hibernate • PostgreSQL

---

# ⭐ Project Highlights

This project demonstrates a complete backend development workflow:

```text
REST API
   ↓
Spring Boot
   ↓
Controller Layer
   ↓
Service Layer
   ↓
Spring Data JPA
   ↓
Hibernate
   ↓
PostgreSQL
```

### Key Highlights

* Built a complete RESTful URL shortening backend
* Implemented unique random short-code generation
* Added CRUD operations for shortened URLs
* Implemented HTTP 301 redirection
* Added access-count tracking
* Implemented URL validation
* Added centralized global exception handling
* Used DTOs to separate API models from database entities
* Used Spring Data JPA and Hibernate for database persistence
* Tested REST endpoints using Postman

---

## 📄 License

This project is created for **educational and portfolio purposes**.
