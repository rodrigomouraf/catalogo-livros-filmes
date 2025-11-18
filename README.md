# 📚 Catalog of Books, Series and Movies

A simple Java web application for cataloging books, series, and movies — built with **Servlets**, **JSP**, and **JDBC**.  
Originally created for academic purposes, but structured to resemble a real-world Java EE (Jakarta EE) web app.

---

## 🎯 Learning Objectives

This project demonstrates:

- Object-Oriented Programming (OOP) in Java
- Web development using **Jakarta Servlets** and **JSP**
- Database access using **JDBC + PreparedStatement**
- Secure SQL handling and modular architecture
- Docker-based setup for MySQL and Tomcat

---

## 🧰 Tech Stack

| Component | Technology |
|------------|-------------|
| Language | Java 21 |
| Build Tool | Maven |
| Frameworks | Jakarta Servlet 6, JSP, JSTL |
| Database | MySQL 8 |
| Server | Apache Tomcat 10 |
| Containerization | Docker / Docker Compose |

---

## ⚙️ Project Structure

catalogo-livros-filmes/
├── src/
│ ├── main/
│ │ ├── java/br/com/catalogo_livros_filmes/
│ │ │ ├── api/controllers/ # Servlets (controllers)
│ │ │ ├── shared/database/ # ConnectionFactory & DatabaseMigrator
│ │ │ ├── shared/models/ # POJOs (CatalogModel)
│ │ │ ├── shared/repositories/ # JDBC Repositories
│ │ └── resources/
│ │ ├── db.properties # Database connection config
│ │ ├── db/migrations/ # SQL migrations
│ │ └── db/seeds/ # SQL seed data
│ └── webapp/
│ └── WEB-INF/views/items/ # JSP views
│ └── list.jsp
├── pom.xml
├── Dockerfile
└── docker-compose.yml

yaml
Copiar código

---

## 🗄️ Database Configuration

The application reads its configuration from `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/catalog_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=secret
db.driver=com.mysql.cj.jdbc.Driver
You can override these using environment variables (especially in Docker):

Variable	Description
DB_URL	JDBC connection string
DB_USERNAME	Database username
DB_PASSWORD	Database password

💡 Tip: Environment variables take precedence over the file values.

🧱 Database Initialization (Migrations + Seeds)
Before running the web app, initialize your database schema and seed data:

▶ Option A – Run from IntelliJ
Open br.com.catalogo_livros_filmes.shared.database.DatabaseMigrator

Right-click → Run 'DatabaseMigrator.main()'

▶ Option B – Run from command line
bash
Copiar código
mvn clean compile
java -cp target/classes br.com.catalogo_livros_filmes.shared.database.DatabaseMigrator
This process executes:

db/migrations/V1__create_catalog_table.sql

db/seeds/S1__seed_catalog.sql

🐳 Running with Docker
You can run the full environment (MySQL + WebApp) using Docker Compose.

1️⃣ Build and start containers:
bash
Copiar código
docker compose up -d --build
2️⃣ Access the app:
🌐 http://localhost:8081/catalog

3️⃣ MySQL info:
Key	Value
Host	catalog-mysql
Port	3306
Database	catalog_db
User	catalog_user
Password	secret

The app container (catalog-web) connects automatically using these variables.

🔧 Building Manually
If you just want to build the WAR (without Docker):

bash
Copiar código
mvn clean package
This generates:

bash
Copiar código
target/catalogo-livros-filmes.war
You can then deploy it manually to a Tomcat 10+ server.

💾 Data Persistence
MySQL data is persisted in a Docker volume (mysql_data), defined in docker-compose.yml.
You can safely restart containers without losing data.

If needed to reset:

bash
Copiar código
docker compose down -v
🧩 JSTL & JSP
The project uses Jakarta JSTL 3.0 with Jakarta namespaces:

jsp
Copiar código
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
Ensure the dependencies below are present in your pom.xml:

xml
Copiar código
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
    <version>3.0.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>3.0.1</version>
</dependency>
🧠 Useful Commands
Action	Command
Build app	mvn clean package
Start environment	docker compose up -d --build
Stop environment	docker compose down
Open MySQL shell	docker exec -it catalog-mysql mysql -u catalog_user -psecret
View Tomcat logs	docker logs -f catalog-web

🧑‍💻 Author: Rodrigo Moura Ferreira
📦 Project: catalogo-livros-filmes