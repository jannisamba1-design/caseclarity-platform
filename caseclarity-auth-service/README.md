
🔐 CaseClarity – Auth Domain Service
A reactive, secure authentication and authorization microservice built using Spring Boot WebFlux, JWT, and RBAC, designed following enterprise microservices architecture patterns (inspired by Verizon SOE architecture).

📌 Overview
The Auth Domain Service is responsible for:
User registration (Signup)
User authentication (Login)
JWT access & refresh token generation
Role-based authorization (RBAC)
Admin-level user role management
Secure endpoint protection
This service is stateless, reactive, and designed for horizontal scalability.

🛠 Technology Stack
Layer	Technology
Language	Java 21
Framework	Spring Boot 3.2.5
Web	Spring WebFlux
Security	Spring Security (Reactive)
Auth	JWT (Access + Refresh tokens)
Persistence	PostgreSQL (R2DBC)
Build Tool	Maven
Testing	JUnit 5, Mockito, WebTestClient
Coverage	JaCoCo
Containerization	Docker (optional)

🔐 Security Features
JWT-based stateless authentication
Access & Refresh token flow
Role-based authorization (USER, ADMIN)
Method-level security using @PreAuthorize
Custom JWT authentication filter
Secure reactive SecurityContext

🚀 API Endpoints
Public Auth APIs
Method	Endpoint	Description
POST	/internal/auth/signup	User registration
POST	/internal/auth/login	User login
POST	/internal/auth/refresh	Refresh access token
Secured APIs
Method	Endpoint	Role
GET	/secure/user	USER
GET	/secure/admin	ADMIN
PATCH	/internal/admin/users/{id}/role	ADMIN

▶️ Running the Application
Prerequisites
Java 21
Maven 3.9+
PostgreSQL (Docker recommended)
Run PostgreSQL (Docker)
docker run -d \
--name auth-postgres \
-e POSTGRES_DB=auth_db \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=password \
-p 5432:5432 postgres:15

Run Application
mvn spring-boot:run

👨‍💻 Author
CaseClarity Platform
Designed as an enterprise-grade interview preparation project covering system design, microservices, reactive programming, and security best practices.
