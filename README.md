# Ecommerce Service Ecosystem

A full-stack e-commerce application featuring a robust Spring Boot microservice architecture and a dynamic Angular user interface.

## 🚀 Key Performance Optimization
* **JPA N+1 Solution:** Optimized critical database queries using **JPA EntityGraph** on Order fetching logic, reducing database roundtrips from $O(N+1)$ to $O(1)$ and significantly boosting application throughput under heavy loads.

---

## 📂 Project Architecture

The workspace is organized as a unified monorepo:

* **`/ecommerce-service`**: Spring Boot application (REST API, Spring Data JPA, Spring Security).
* **`/ecommerce-service-frontend`**: Angular web client responsive user interface.
* **`/database`**: Database schema initialization scripts and `.sql` dumps.

---

## 🛠️ Tech Stack

### Backend
* Java 17 / Spring Boot 3.x
* Spring Data JPA & Hibernate
* MySQL (Database)
* Maven (Dependency Management)

### Frontend
* Angular
* TypeScript
* Bootstrap / Tailwind CSS (UI Styling)

---

## ⚙️ Local Setup Instructions

### 1. Database Configuration
1. Create a database named `ecommerce_db` in your local SQL server.
2. Import the schema setup script found in the `/database` directory.

### 2. Run the Backend
Before launching, make sure to set your secure database password as a local environment variable (`DB_PASSWORD`).

```bash
cd backend
# Set environment password locally (for Windows MINGW64)
export DB_PASSWORD="your_database_password"

# Run the Spring Boot application
./mvnw spring-boot:run
