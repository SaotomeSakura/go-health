# GoHealth Ticket Manager

A Java application for managing support tickets using Google Sheets as a database.  
Supports both a **Command-Line Interface (CLI)** and a **REST API**, giving users the flexibility to choose how they interact with the system.

---

## 🚀 Features
- Create new tickets
- Update ticket status
- List tickets by status
- Dual-mode: CLI and REST API
- Google Sheets integration for persistent storage

---

## 🛠 Tech Stack
- Java 17
- Spring Boot
- Docker
- Google Sheets API

---

## Getting Started
1. Clone the repo
2. Build with `mvn clean package`
For the CLI app
3. Run with `java -jar target/GoHealth-1.0-SNAPSHOT.jar`
4. Or use Docker: `docker build -t ticket-cli .`
For the REST
5. Launch app (starts web server on port 8081)
6. Access endpoints via Postman or curl: For endpoint details, see the OpenAPI documentation 


## Usage for CLI app
# Create a ticket
java -jar app.jar --create --description "Something broke"

# Update a ticket
java -jar app.jar --update --id AD-UUID --status CLOSED

# List tickets by status
java -jar app.jar --list --status IN_PROGRESS


## 🐳 Docker Usage

Build the image:
docker build -t ticket-cli .

Run the CLI:
docker run ticket-cli --list --status OPEN

Run the REST API:
java -jar target/GoHealth-1.0-SNAPSHOT.jar

---

 **Note**

## Credentials
This app uses a Google service account to access the Sheets API.  
Make sure `credentials.json` is placed in the project root (Not included in the Git) 

## 🧪 Testing
- **Unit Tests**: Written using JUnit 5 and Mockito
- **Functional Tests**: CLI and REST flows tested
- **Postman Collection**: Included for REST API testing
