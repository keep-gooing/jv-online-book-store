# Online Book Store

## Project Overview

You are watching **Online Book Store** project. This application is designed to simplify the process of buying books online.
It addresses the challenges of finding and purchasing books by offering a user-friendly interface for both customers and administrators.
Users can browse books, search them by category, add them to the shopping cart, and place orders, while administrators manage the catalog of available books.

## Technologies Used

- **Spring Boot** - Backend framework for building the core application
- **Spring Security** - For managing authentication and authorization
- **Spring Data JPA** - For interacting with the database
- **MySQL** - Database to store book and user information
- **Docker** - Containerization for easy deployment
- **Swagger** - API documentation and testing
- **MapStruct** - Object mapping
- **Liquibase** - Database change management
- **JUnit & Mockito** - For unit testing
- **Testcontainers** - For integration testing with real databases

- ## Key Functionalities
- ### For users

- **Book browsing**: Browse books and search them by category 
- **Cart management**: Add books to shopping cart, remove them, or adjust the desired quantity.
- **Order management**: Place orders for the books in shopping cart and track their status. 

### For admins:
- **Book management**: Add books, change information about them, delete them.
- **Category management**: Add categories, change information about them, delete them.
- **Order management**: Update orders status
  
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

2. Create .evn file for environment by filling the .env.template
       ```
    MYSQLDB_DATABASE=<your_database_name>
    MYSQLDB_USER=<your_username>
    MYSQLDB_PASSWORD=<your_password>
    MYSQLDB_ROOT_PASSWORD=<your_root_password>

    SPRING_DATASOURCE_PORT=<your_spring_datasource_port>
    MYSQLDB_PORT=<your_mysql_port>

    SPRING_DATASOURCE_PORT=<your_spring_datasource_port>
    MYSQLDB_LOCAL_PORT=<your_local_port>
    MYSQLDB_DOCKER_PORT=<your_docker_port>
    SPRING_LOCAL_PORT=<your_spring_local_port>
    SPRING_DOCKER_PORT=<your_spring_docker_port>
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
Swagger UI is available for testing the API and is accessible at `http://localhost:8080/swagger-ui.html`. 
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


---
This project was a great learning experience and showcases my ability to build a back-end of application with modern Java technologies.
If you're interested in the project and want to see it in action, I've included a showcase video demonstrating the application usage: https://www.loom.com/share/4122324a84c04dd681c20bd5eacbaf0d?sid=ab33800a-9aaa-49b0-94b0-4a769dfe8e23

