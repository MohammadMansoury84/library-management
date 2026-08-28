# 📚 Library Management System

A REST API for managing a library's books, users, and loans, built with **Spring Boot 3.4** and **Java 17**. It supports book catalog management, borrowing/returning workflows with atomic availability tracking, pagination, search, and schema versioning via **Flyway**.

> 👤 Developer: [MohammadMansoury84](https://github.com/MohammadMansoury84)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#️-tech-stack)
- [Project Architecture](#-project-architecture)
- [Folder Structure](#-folder-structure)
- [Domain Model](#-domain-model)
- [Database Schema](#-database-schema)
- [API Endpoints](#-api-endpoints)
- [Validation & Error Handling](#-validation--error-handling)
- [How to Run](#-how-to-run)
- [Known Issues & Important Notes](#️-known-issues--important-notes)
- [Roadmap / Ideas for Improvement](#-roadmap--ideas-for-improvement)
- [License](#-license)

---

## 🎮 Overview

The system models three core entities — **Book**, **User**, and **Loan** — and exposes a REST API to add/search/update/delete books and to borrow/return them. Each book tracks a total copy count and a live available-copy count; borrowing decrements this count atomically at the database level so concurrent requests can't oversell the same copy. All list endpoints are paginated and Flyway manages the database schema through versioned migration scripts.

## ✨ Features

- 📖 **Book catalog management** — create, read, update, delete, and search by title (case-insensitive, partial match)
- 🔁 **Smart book creation** — adding a book with an ISBN that already exists increases its copy count instead of creating a duplicate row
- 🔒 **Unique ISBN validation** — a custom `@UniqueIsbn` Bean Validation constraint checks the database before allowing a new book to be created
- 📦 **Loan workflow** — borrow a book (creates a loan, decrements availability) and return a book (marks the loan returned, increments availability)
- ⚛️ **Race-condition-safe availability tracking** — copy counts are updated with atomic, conditional SQL (`UPDATE ... WHERE available_amount > 0`) rather than read-then-write in application code
- 🚫 **Duplicate-borrow prevention** — a user cannot borrow the same book twice while an existing loan is still active
- 📄 **Pagination everywhere** — book listing, book search, and per-user loan history all return a paginated envelope (`PageResponseDTO`) with page metadata
- 🌱 **Database versioning with Flyway** — four migration scripts build up the schema incrementally (books → users → loans → ISBN column)
- ⚠️ **Centralized error handling** — a `@RestControllerAdvice` global exception handler returns consistent JSON error bodies for not-found, validation, and business-rule errors
- 🧬 **DTO/entity separation** — MapStruct-generated mappers keep request/response DTOs decoupled from JPA entities
- 👥 **Role model** — a `Role` enum (`ADMIN`, `NORMAL`) exists on the `User` entity for future authorization use

## 🛠️ Tech Stack

| Technology | Role |
|---|---|
| **Java 17** | Language / runtime |
| **Spring Boot 3.4** (`spring-boot-starter-web`) | REST API framework |
| **Spring Data JPA** / **Hibernate** | ORM and repository layer |
| **Bean Validation** (`spring-boot-starter-validation`) | Request DTO validation, incl. a custom `@UniqueIsbn` constraint |
| **MySQL** (`mysql-connector-j`) | Primary database |
| **H2** | In-memory database (runtime dependency, e.g. for local/testing use) |
| **Flyway** (`flyway-core`, `flyway-mysql`) | Database schema migrations |
| **OpenCSV** | CSV parsing (DTOs present; import feature not yet wired up — see [Known Issues](#️-known-issues--important-notes)) |
| **Lombok** | Boilerplate reduction (`@Data`, `@Builder`, `@RequiredArgsConstructor`, etc.) |
| **MapStruct** | Compile-time entity ↔ DTO mapping |
| **Maven** (with wrapper `mvnw`/`mvnw.cmd`) | Build tool |

## 🏗️ Project Architecture

The project follows a standard **layered Spring Boot architecture**:

```
Controller  → REST endpoints (@RestController), request/response handling
Service     → Business logic, interfaces + Impl classes (BookService/BookServiceImp, LoanService/LoanServiceImp)
Repository  → Spring Data JPA repositories (BookRepository, LoanRepository, UserRepository)
Model       → JPA entities (Book, User, Loan, and the LoanStatus/Role enums)
Dto         → Request/response data transfer objects, decoupled from entities
Mapper      → MapStruct interfaces converting between entities and DTOs
Validation  → Custom Bean Validation constraint (@UniqueIsbn) + validation groups (OnCreate/OnUpdate)
Exception   → Custom exceptions + a global @RestControllerAdvice handler
```

Notable architectural details:
- Services depend on **interfaces** (`BookService`, `LoanService`) implemented by `BookServiceImp`/`LoanServiceImp`, following the classic Spring interface/implementation split.
- **Optimistic locking**: the `Book` entity has a `@Version` field, guarding against lost updates on concurrent edits.
- **Availability tracking uses atomic SQL updates** (`@Modifying @Query("UPDATE Book b SET b.availableAmount = b.availableAmount - 1 WHERE b.id = :id AND b.availableAmount > 0")`) rather than a read-modify-write cycle, so the borrow operation is safe under concurrent requests.
- **Validation groups** (`ValidationGroups.OnCreate` / `OnUpdate`) let `BookRequestDTO` apply the `@UniqueIsbn` check only on creation, not on update.
- All list-returning endpoints accept a Spring Data `Pageable` and respond with a custom `PageResponseDTO<T>` wrapper exposing page number, size, total pages/elements, and first/last/hasNext/hasPrevious flags.

## 📁 Folder Structure

```
src/main/java/com/example/library_management_system/
├── Controller/
│   ├── BookController.java        # /api/books — CRUD + search
│   └── LoanController.java        # /api/loans — borrow / return / user history
├── Dto/
│   ├── BookRequestDTO.java / BookResponseDTO.java
│   ├── LoanRequestDTO.java / LoanResponseDTO.java
│   ├── UserRequestDto.java
│   ├── PageResponseDTO.java       # Generic pagination envelope
│   └── CsvBookDto.java / CsvImportResult.java   # CSV import scaffolding (not yet wired to a controller)
├── Exception/
│   ├── BookNotAvailableException.java
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java   # @RestControllerAdvice — consistent JSON error responses
├── Mapper/
│   ├── BookMapper.java             # MapStruct: Book <-> BookResponseDTO
│   └── LoanMapper.java             # MapStruct: Loan <-> LoanResponseDTO
├── Model/
│   ├── Book.java                   # id, title, isbn, totalCopies, availableAmount, available, @Version
│   ├── Loan.java                   # id, user, book, status, loanDate, returnDate
│   ├── LoanStatus.java             # enum: ACTIVE, RETURNED, OVERDUE
│   ├── Role.java                   # enum: NORMAL, ADMIN
│   └── User.java                   # id, userName, email, password, role
├── Validation/
│   ├── UniqueIsbn.java              # Custom constraint annotation
│   ├── UniqueIsbnValidator.java     # Checks BookRepository for ISBN collisions
│   └── ValidationGroups.java        # OnCreate / OnUpdate marker interfaces
├── repository/
│   ├── BookRepository.java          # incl. atomic increase/decreaseAvailableAmount queries
│   ├── LoanRepository.java
│   └── UserRepository.java
├── service/
│   ├── BookService.java / BookServiceImp.java
│   └── LoanService.java / LoanServiceImp.java
└── LibraryManagementSystemApplication.java   # @SpringBootApplication entry point

src/main/resources/
├── application.properties
└── db/migration/
    ├── V1__create_books_table.sql
    ├── V2__create_users_table.sql
    ├── V3__create_loans_table.sql
    └── V4__add_isbn_column.sql

src/test/java/.../LibraryManagementSystemApplicationTests.java   # Default Spring context-load test
```

## 🧩 Domain Model

| Entity | Key fields | Notes |
|---|---|---|
| **Book** | `title`, `isbn` (unique), `totalCopies`, `availableAmount`, `available`, `version` | `@OneToMany` to `Loan`; optimistic locking via `@Version` |
| **User** | `userName`, `email`, `password`, `role` | `@OneToMany` to `Loan`; `role` is `NORMAL` or `ADMIN` |
| **Loan** | `user` (`@ManyToOne`), `book` (`@ManyToOne`), `status`, `loanDate`, `returnDate` | `status` is one of `ACTIVE`, `RETURNED`, `OVERDUE` in the Java enum |

## 🗄️ Database Schema

Managed by Flyway, in `src/main/resources/db/migration/`:

| Migration | Effect |
|---|---|
| `V1__create_books_table.sql` | Creates `books` (`title`, `available`, `total_copies`, `available_amount`) with `CHECK` constraints ensuring `available_amount <= total_copies` and `total_copies > 0` |
| `V2__create_users_table.sql` | Creates `users` (`user_name`, unique `email`, `password`, `role` ENUM `('ADMIN','NORMAL')`) |
| `V3__create_loans_table.sql` | Creates `loan` with foreign keys to `users` and `books` (both `ON DELETE CASCADE`), plus a `status` ENUM `('BORROWED','RETURNED','OVERDUE')` |
| `V4__add_isbn_column.sql` | Adds a unique `isbn` column to `books` |

Naming strategy: `CamelCaseToUnderscoresNamingStrategy` — Java camelCase fields map to snake_case columns automatically.

## 🌐 API Endpoints

### Books — `/api/books`
| Method | Path | Description |
|---|---|---|
| `POST` | `/api/books` | Add a book. If the ISBN already exists, increases its copy count instead of creating a duplicate |
| `GET` | `/api/books` | List books, paginated (default: 10/page, sorted by title). Add `?title=...` to search by title instead |
| `GET` | `/api/books/{id}` | Get a single book by ID |
| `PUT` | `/api/books/{id}` | Update a book's title, ISBN, and total copies |
| `DELETE` | `/api/books/{id}` | Delete a book |

### Loans — `/api/loans`
| Method | Path | Description |
|---|---|---|
| `POST` | `/api/loans` | Borrow a book (`userId`, `bookId` in the body). Fails if the book has no available copies or the user already has it on loan |
| `PUT` | `/api/loans/{loanId}/return` | Return a borrowed book |
| `GET` | `/api/loans/user/{userId}` | Get a user's loan history, paginated |

> There is currently no `UserController`/`/api/users` endpoint — users must be created some other way (e.g. directly in the database or a repository call) since no registration route is exposed. See [Known Issues](#️-known-issues--important-notes).

## ✅ Validation & Error Handling

- Request DTOs are validated with Jakarta Bean Validation (`@NotBlank`, `@Size`, `@Min`/`@Max`, `@Pattern`, `@Email`, `@FutureOrPresent`, etc.).
- `BookRequestDTO.isbn` is checked against an ISBN-format regex and, on creation only (`ValidationGroups.OnCreate`), against the custom `@UniqueIsbn` constraint.
- `GlobalExceptionHandler` (`@RestControllerAdvice`) turns exceptions into a consistent JSON error shape (`timestamp`, `status`, `error`, `massage`, `path`) for:
  - `ResourceNotFoundException` → `404`
  - `BookNotAvailableException` → `400`
  - `MethodArgumentNotValidException` (failed `@Valid`/`@Validated` checks) → `400`, with a `FieldError` map of field → message
  - any other unhandled `Exception` → `500`

## 🚀 How to Run

### Prerequisites
- **Java 17**
- A **MySQL** server (unless you rely on the H2 runtime dependency instead)

### 1. Configure the database
By default (`src/main/resources/application.properties`), the app expects:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
```
Create the database first:
```sql
CREATE DATABASE library_db;
```
Flyway will automatically create/update the schema (`spring.jpa.hibernate.ddl-auto=validate`, so Hibernate only *validates* the schema — Flyway is what actually creates the tables) on startup.

### 2. Run the application
Using the included Maven wrapper:
```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```
Or build a jar and run it directly:
```bash
./mvnw clean package
java -jar target/library_management_system-0.0.1-SNAPSHOT.jar
```

The API will be available at:
```
http://localhost:7070/api/books
http://localhost:7070/api/loans
```
(Port `7070` is set via `server.port` in `application.properties`.)

### 3. Try it out
```bash
# Add a book
curl -X POST http://localhost:7070/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","totalCopies":3,"isbn":"9780132350884"}'

# List books
curl http://localhost:7070/api/books

# Borrow a book
curl -X POST http://localhost:7070/api/loans \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"bookId":1}'
```

## ⚠️ Known Issues & Important Notes

- 🔴 **No authentication/authorization layer**: `User.role` (`ADMIN`/`NORMAL`) exists on the entity, but there is no Spring Security dependency and no enforcement anywhere in the codebase — any client can call any endpoint.
- 🔴 **No user-facing endpoint**: there's a `UserRequestDto` and a `UserRepository`, but no `UserController`, so there's currently no REST way to register/create a user.
- 🟠 **`LoanStatus` enum mismatch with the database**: the Java enum is `ACTIVE, RETURNED, OVERDUE`, while the `V3` migration defines the SQL column as `ENUM('BORROWED','RETURNED','OVERDUE')` — `ACTIVE` (Java) and `BORROWED` (SQL) don't match, which will cause a mapping error if MySQL's schema is used as-is with the current entity.
- 🟠 **CSV import is incomplete**: `CsvBookDto` (with OpenCSV bindings) and `CsvImportResult` (currently an empty class) exist, and `opencsv` is a declared dependency, but no controller or service method uses them — the bulk-import-books-from-CSV feature is scaffolded but not implemented.
- 🟡 **Duplicated/malformed validation annotation**: `BookRequestDTO.isbn` has two stacked `@Pattern` annotations — one with a real ISBN regex, and a second with a placeholder regex (`"..."`) that would reject nearly all input if it were the only one enforced; likely leftover from editing.
- 🟡 **Hardcoded local datasource credentials**: DB URL/username/password sit directly in `application.properties` (root user, empty password) rather than environment variables or a secrets mechanism — fine for local development, not for shipping as-is.
- 🟡 **Minor typos in error responses**: the global exception handler's JSON error field is named `"massage"` instead of `"message"`, and the generic `500` handler returns the message `"radios server error"`.

## 🧭 Roadmap / Ideas for Improvement

- Add Spring Security with role-based access control (protect admin-only endpoints like book creation/deletion)
- Add a `UserController` with registration/login endpoints and password hashing
- Finish the CSV bulk-import feature for books
- Reconcile the `LoanStatus` enum with the database `ENUM` definition
- Move datasource credentials to environment variables / Spring profiles
- Add integration tests beyond the default context-load test
- Add OpenAPI/Swagger documentation for the REST endpoints

## 📄 License

No license has been specified for this repository. If you'd like to make the code available for general use, adding a `LICENSE` file (e.g. MIT) is recommended.

---

<div align="center">

Built with ☕ and Spring Boot by [Mohammad Mansoury](https://github.com/MohammadMansoury84)

</div>
