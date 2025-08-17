# Online Book Store

## Project Overview

The **Online Book Store** is a full-featured application for purchasing books online.
It addresses the challenges of finding and purchasing books by offering a user-friendly interface for both customers and administrators.
Users can browse books, search them by category, add them to the shopping cart, and place orders, while administrators manage the catalog of available books.

## Technologies Used

- **Java** 17 – Programming language
- **Spring Boot** 3.2.0 - Backend framework for building the core application
- **Spring Security** 6.2.x - For managing authentication and authorization
- **Spring Data JPA** 3.2.x - For interacting with the database
- **PostgreSQL** 15 - Database to store book and user information
- **Docker** - Containerization for easy deployment
- **Swagger** 2.5.0 - API documentation and testing
- **MapStruct** 1.5.5 Final - Object mapping
- **Liquibase** 4.x - Database change management
- **JUnit & Mockito** 5 / 5.18.0 - For unit testing
- **Testcontainers** 1.20.1 - For integration testing with real databases

- ## Key Functionalities
- ### For users

- **Book browsing**: Browse books and search them by category 
- **Cart management**: Add books to shopping cart, remove them, or adjust the desired quantity.
- **Order management**: Place orders for the books in shopping cart and track their status. 

### For admins:
- **Book management**: Add books, change information about them, delete them.
- **Category management**: Add categories, change information about them, delete them.
- **Order management**: Update orders status

### Model diagram

<img width="1402" height="1058" alt="image" src="https://github.com/user-attachments/assets/14122e5e-70a6-466e-94fa-05b635c4063b" />
  
## How to Run the Project

### Requirements

- **Java** version 17 and higher
- **Maven** for dependency management
- **Docker** and **Docker Compose** for setting up the environment

- ### Launch application:

1. Clone the repository:
    ```bash
    git clone git@github.com:keep-gooing/online-book-store.git
    cd online-book-store
    ```

2.  Create the .env file by copying .env.template and replacing placeholder values:
       ```
      POSTGRES_USER=<your_postgres_user>
      POSTGRES_PASSWORD=<your_postgres_password>
      POSTGRES_DATABASE=<your_database_name>
      POSTGRES_LOCAL_PORT=<your_local_port>
      POSTGRES_DOCKER_PORT=<your_docker_port>

      SPRING_LOCAL_PORT=<your_spring_local_port>
      SPRING_DOCKER_PORT=<your_spring_docker_port>
      DEBUG_PORT=<your_debug_port>

      JWT_EXPIRATION=<your_jwt_expiration>
      JWT_SECRET=<your_jwt_secret>
    ```

3. Build and start the containers using Docker Compose:
    ```bash
    docker-compose up --build
    ```

4. The application will be accessible at `http://localhost:<YOUR_PORT>/api`.

### Running Tests:

1. To run unit and integration tests using Testcontainers, execute:
    ```bash
    mvn clean test
    ```
Swagger UI is available for testing the API and is accessible at `http://localhost:8080/api/swagger-ui/index.html#/`. 
It includes endpoints for all available operations, such as browsing books, managing the cart, and handling orders.

## Challenges Faced

Challenges Faced During Development
Database Migration Difficulties
Issue: Encountered ***complications*** with database schema creation while using Liquibase for change management. The application ***failed*** to generate required tables.
Resolution: Conducted a thorough ***review*** of the YAML-based Liquibase scripts and tested them in a ***local environment***, which helped identify and fix errors. As a result, migrations were successfully applied without further issues.

Testing Environment with Docker and Testcontainers
Issue: Faced ***complexities*** in setting up a Docker-based testing environment, especially regarding ***database*** connectivity and lifecycle.
Resolution: Explored official documentation and real-world ***examples for Testcontainers***. This allowed proper configuration of containers, ensuring tests executed correctly in an ***isolated environment***.

Spring Security Configuration
Issue: Experienced ***access control*** errors due to misconfigured Spring Security settings, which led to unauthorized or blocked requests.
Resolution: Refactored the security setup by incorporating the appropriate ***filters, URL patterns, and user roles***. These changes resolved the access problems and enforced the required ***security standards***.

## Testing the API with Postman

Import the [Postman collection](https://olhaoleshchuk.postman.co/workspace/Olha-Oleshchuk's-Workspace~788862a7-cf6f-4a81-8d4b-23606e2677b9/request/45714411-3cf61d86-c479-4561-8d31-3e6d08349473?action=share&source=copy-link&creator=45714411) to test API endpoints.
Set localhost:8080/api as the base URL for all requests.

---
This project was a great learning experience and showcases my ability to build a back-end of application with modern Java technologies.
If you're interested in the project and want to see it in action, I've included a showcase video demonstrating the application usage: https://www.loom.com/share/4122324a84c04dd681c20bd5eacbaf0d?sid=ab33800a-9aaa-49b0-94b0-4a769dfe8e23

