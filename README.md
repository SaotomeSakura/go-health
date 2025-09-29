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
### 🖥️ CLI Mode
1. Clone the repo
2. Build with `mvn clean package`
3. Run with `java -jar target/GoHealth-1.0-SNAPSHOT.jar`
4. Or use Docker: `docker build -t ticket-cli .`

### 🌐 REST API Mode
1. Clone the repo
2. Build with `mvn clean package`
3. Run with `java -jar target/GoHealth-1.0-SNAPSHOT.jar`
4. Access endpoints via Postman or curl (server runs on port `8081`)
5. See OpenAPI documentation for endpoint details


## 🖥️ CLI Usage Examples

### Create a ticket
```bash
java -jar app.jar --create --description "Something broke"
```

### Update a ticket
```bash
java -jar app.jar --update --id AD-UUID --status CLOSED
```

### List tickets by status
```bash
java -jar app.jar --list --status IN_PROGRESS
```

## 🐳 Docker Usage

### Build the image
```bash
docker build -t ticket-cli .
```

### Run the CLI:
```bash
docker run ticket-cli --list --status OPEN
```

---

 **Note**

```markdown
## Credentials
This app uses a Google service account to access the Sheets API.  
Make sure `credentials.json` is placed in the project root (Not included in the Git) 
```

## 🧪 Testing
- **Unit Tests**: Written using JUnit 5 and Mockito
- **Functional Tests**: CLI and REST flows tested
- **Postman Collection**: Included for REST API testing
