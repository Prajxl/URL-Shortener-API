URL Shortener API
A RESTful URL Shortening Service built using Java, Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL.

The application allows users to create short URLs from long URLs, retrieve and update URLs, delete short URLs, track access counts, and redirect users to the original URL.

🚀 Features
Create short URLs

Generate unique random short codes

Retrieve original URL details

Update an existing short URL

Delete short URLs

Redirect short URLs to original URLs

Track the number of times a short URL is accessed

URL validation

Global exception handling

PostgreSQL database persistence

RESTful API architecture

🛠️ Tech Stack
TechnologyPurpose	
Java	Programming Language
Spring Boot	Backend Framework
Spring Web	REST API development
Spring Data JPA	Database access
Hibernate	ORM
PostgreSQL	Relational Database
Jakarta Validation	Request validation
Maven	Dependency Management
Postman	API Testing
📁 Project Structure
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

🏗️ Architecture
The application follows a layered architecture:

Client / Postman
       │
       ▼
┌──────────────────┐
│    Controller    │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│     Service      │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│    Repository    │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│    PostgreSQL    │
└──────────────────┘

Controller
Handles HTTP requests and responses.

Service
Contains the application's business logic, including short-code generation, URL updates, deletion, and access-count tracking.

Repository
Uses Spring Data JPA to communicate with PostgreSQL.

Entity
Represents the urls table in PostgreSQL.

DTO
Separates API request/response data from the database entity.

Utility
Generates random unique short codes.

Exception
Provides centralized exception handling and meaningful API error responses.

🗄️ Database
The application uses PostgreSQL.

Create the database:

CREATE DATABASE url_shortener;

The main table is automatically created by Hibernate.

URL Table
urls
------------------------------------------------
id
original_url
short_code
created_at
updated_at
access_count

Example Record
id:            1
original_url:  https://www.google.com
short_code:    RC0mJ1
created_at:    2026-08-17T16:50:56
updated_at:    2026-08-17T16:50:56
access_count:  3

⚙️ Configuration
Update application.properties:

spring.application.name=url-shortener

spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080

Replace YOUR_PASSWORD with your PostgreSQL password.

🔌 API Endpoints
1. Create Short URL
Request
POST /shorten

Example
POST http://localhost:8080/shorten

Request Body
{
    "url": "https://www.google.com"
}

Response
201 Created

{
    "id": 1,
    "url": "https://www.google.com",
    "shortCode": "RC0mJ1",
    "createdAt": "2026-08-17T16:50:56.121588",
    "updatedAt": "2026-08-17T16:50:56.121588"
}

The shortCode is generated randomly and must be unique.

2. Retrieve URL
Request
GET /shorten/{shortCode}

Example
GET http://localhost:8080/shorten/RC0mJ1

Response
200 OK

{
    "id": 1,
    "url": "https://www.google.com",
    "shortCode": "RC0mJ1",
    "createdAt": "2026-08-17T16:50:56.121588",
    "updatedAt": "2026-08-17T16:50:56.121588"
}

If the short code doesn't exist:

404 Not Found

{
    "status": 404,
    "message": "No URL found for short code: RC0mJ1",
    "timestamp": "2026-08-17T16:55:00"
}

3. Update Short URL
Request
PUT /shorten/{shortCode}

Example
PUT http://localhost:8080/shorten/RC0mJ1

Request Body
{
    "url": "https://www.youtube.com"
}

Response
200 OK

{
    "id": 1,
    "url": "https://www.youtube.com",
    "shortCode": "RC0mJ1",
    "createdAt": "2026-08-17T16:50:56.121588",
    "updatedAt": "2026-08-17T17:10:20.121588"
}

The short code remains unchanged.

RC0mJ1
   ↓
https://www.google.com

        UPDATE

RC0mJ1
   ↓
https://www.youtube.com

4. Delete Short URL
Request
DELETE /shorten/{shortCode}

Example
DELETE http://localhost:8080/shorten/RC0mJ1

Response
204 No Content

If the short code doesn't exist:

404 Not Found

5. Get URL Statistics
Request
GET /shorten/{shortCode}/stats

Example
GET http://localhost:8080/shorten/RC0mJ1/stats

Response
200 OK

{
    "id": 1,
    "url": "https://www.google.com",
    "shortCode": "RC0mJ1",
    "createdAt": "2026-08-17T16:50:56.121588",
    "updatedAt": "2026-08-17T16:50:56.121588",
    "accessCount": 5
}

The accessCount represents how many times the short URL has been used for redirection.

6. Redirect to Original URL
Request
GET /{shortCode}

Example
GET http://localhost:8080/RC0mJ1

The application:

RC0mJ1
   ↓
Find URL in PostgreSQL
   ↓
Increment accessCount
   ↓
Return HTTP 301
   ↓
Redirect to original URL

For example:

http://localhost:8080/RC0mJ1
              ↓
https://www.google.com

🔄 URL Shortening Flow
Client
  │
  │ POST /shorten
  │
  │ {"url":"https://www.google.com"}
  ▼
UrlController
  │
  ▼
UrlService
  │
  ├── Generate random short code
  │
  ├── Check short-code uniqueness
  │
  ├── Create Url entity
  │
  ▼
UrlRepository
  │
  ▼
PostgreSQL
  │
  ▼
UrlResponse
  │
  ▼
Client

🔀 Redirect Flow
Browser
   │
   │ GET /RC0mJ1
   ▼
UrlController
   │
   ▼
UrlService
   │
   ├── Find RC0mJ1
   │
   ├── accessCount++
   │
   ▼
PostgreSQL
   │
   ▼
HTTP 301 Redirect
   │
   ▼
Original URL

🧪 Testing with Postman
Recommended testing sequence:

1. POST    /shorten
2. GET     /shorten/{shortCode}
3. PUT     /shorten/{shortCode}
4. GET     /shorten/{shortCode}/stats
5. GET     /{shortCode}
6. GET     /shorten/{shortCode}/stats
7. DELETE  /shorten/{shortCode}
8. GET     /shorten/{shortCode}

After using the redirect endpoint, verify that accessCount has increased.

▶️ How to Run
1. Clone the repository
git clone <your-github-repository-url>

2. Open the project
Open it using:

IntelliJ IDEA

Eclipse

VS Code

3. Configure PostgreSQL
Create:

CREATE DATABASE url_shortener;

Update the database credentials in:

src/main/resources/application.properties

4. Run the application
Using Maven:

mvn spring-boot:run

Or run:

UrlShortenerApplication.java

5. Application URL
http://localhost:8080

📌 HTTP Status Codes
StatusMeaning	
200 OK	Request successful
201 Created	Short URL successfully created
204 No Content	URL successfully deleted
400 Bad Request	Invalid URL/request
404 Not Found	Short URL doesn't exist
301 Moved Permanently	Redirect to original URL
🔐 Validation
The API validates the URL before processing it.

Valid:

{
    "url": "https://www.google.com"
}

Valid:

{
    "url": "http://example.com"
}

Invalid:

{
    "url": ""
}

Invalid:

{
    "url": "google.com"
}

Invalid requests return:

400 Bad Request

🧠 Key Spring Boot Concepts Used
This project demonstrates:

RESTful API development

@RestController

@PostMapping

@GetMapping

@PutMapping

@DeleteMapping

@RequestBody

@PathVariable

@Valid

Jakarta Bean Validation

Dependency Injection

Service Layer

Repository Layer

Spring Data JPA

Hibernate ORM

Entity Mapping

PostgreSQL

DTO Pattern

Exception Handling

ResponseEntity

HTTP Status Codes

HTTP 301 Redirect

Transaction Management

🚀 Future Improvements
The current implementation focuses on the core requirements. Future improvements can include:

Custom short-code support

URL expiration

User authentication and authorization

Click history

Detailed analytics

IP/device/browser statistics

Rate limiting

Redis caching

Swagger/OpenAPI documentation

Unit testing with JUnit and Mockito

Integration testing

Docker containerization

CI/CD pipeline

Cloud deployment

👨‍💻 Author
Prajwal K

Computer Science & Engineering Graduate
Java | Spring Boot | REST API | PostgreSQL

⭐ Project Highlights
This project demonstrates a complete backend flow:

REST API
   ↓
Spring Boot
   ↓
Service Layer
   ↓
Spring Data JPA
   ↓
Hibernate
   ↓
PostgreSQL

It also implements URL shortening, unique short-code generation, URL management, HTTP redirection, and access statistics.
