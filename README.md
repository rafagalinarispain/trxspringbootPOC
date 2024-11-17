
# Demo Project: Spring Boot with MongoDB Transactions

This is a demo project showcasing the integration of Spring Boot with MongoDB. It includes multiple methods for updating MongoDB collections using various transaction management techniques. The project is developed and tested using **Visual Studio Code**.

## Prerequisites

- **Java 17**
- **Maven**
- **MongoDB**

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/rafagalinarispain/trxspringbootPOC.git
```

### Build and Run

#### Using Maven
**Build:**
```sh
mvn clean package
```

**Run:**
```sh
mvn spring-boot:run
```

#### Using VSCode
Alternatively, you can use the built-in tools in Visual Studio Code to build and run the project.

### MongoDB Setup

1. **Database and Collections:**  
   Ensure your MongoDB instance contains a database named `db1` with the following collections: `coll1`, `coll2`, `coll3`, `coll4`, and `coll5`. Each collection should follow the schema below:  
   ```javascript
   { _id: ObjectId('6739c4063ac3a50065be690b'), a: 1, b: 20 }
   ```

2. **Profiling and Logging:**  
   To verify how writes are processed and determine if they are attached to a transaction, adjust the profiling level and increase the logging verbosity for the `transaction` component:
   ```javascript
   db.setProfilingLevel(0, { "slowms": 0 });
   db.setLogLevel(1, "transaction");
   ```

---

## Proof of Concept (PoC) Details

### Updating Collections

Once the project is running, you can update collections via `POST` requests. The project provides five endpoints, each targeting a specific collection. Example usage with `curl`:

```sh
curl -X POST http://localhost:8080/update/coll1/10
curl -X POST http://localhost:8080/update/coll2/20
curl -X POST http://localhost:8080/update/coll3/30
curl -X POST http://localhost:8080/update/coll4/40
curl -X POST http://localhost:8080/update/coll5/50
```

### Transaction Mechanisms

1. **coll1 & coll2:**  
   Uses a transaction manager constructed as a Java Bean with the `@Transactional` annotation.
2. **coll3:**  
   Updates the collection with explicit usage of the transaction manager.
3. **coll4:**  
   Updates the collection using a transaction within the `ClientSession` mechanism.
4. **coll5:**  
   Uses `TransactionTemplate` to update the collection.

### Configuration

Edit the `application.properties` file to provide a reachable MongoDB URI:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/your-database
```

---

## Testing with Visual Studio Code

### Running Tests

1. Open the project folder in **VSCode**.
2. Use the built-in test runner to run tests. Click "Run Test" or "Debug Test" above the test method in your test file.
3. View the test results in the **Test Explorer** panel.

### Debugging Tests

1. Set breakpoints in your code by clicking in the gutter next to the line numbers.
2. Click "Debug Test" above the test method or select "Debug" from the context menu.
3. Inspect variables in the **Variables** panel and step through the code using the **Step Over**, **Step Into**, and **Step Out** buttons.

---

## Notes

- Ensure that your MongoDB instance is running before starting the application.
- For security, do not expose sensitive credentials or data while configuring MongoDB URIs.
- This PoC was built by a Spring Boot/Java dummy, so any contributions or suggestions for improvement are warmly welcomed!
