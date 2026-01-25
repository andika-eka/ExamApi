### **Project Lifecycle**

**Load Configs**

```bash
cp ./dev_env.sh.example ./dev_env.sh
source ./dev_env.sh
```


**Start the Application**
Running this will also trigger the `DataSeeder` (if the DB is empty) to generate random exams.

```bash
./mvnw spring-boot:run

```

**Clean & Rebuild**
Use this if you change dependencies or weird caching issues occur.

```bash
./mvnw clean install -DskipTests

```

---

### **Testing**

**Run All Tests**
(Requires Docker to be running for Integration Tests)

```bash
./mvnw test

```

**Run ONLY Unit Tests (Fast, No Docker)**
Perfect for testing Controller logic quickly.

```bash
./mvnw -Dtest=ExamControllerTest test
./mvnw -Dtest=AuthControllerTest test

```

**Run ONLY Integration Tests (Slow, Needs Docker)**
Tests the Database, Seeding, and Repository logic.

```bash
./mvnw -Dtest=ExamIntegrationTest test

```

**Run Specific Test Method**
If you only want to check the "Grading Logic" inside the Integration Test.

```bash
./mvnw -Dtest=ExamIntegrationTest#shouldSavePolymorphicQuestions test

```

---

### **Docker & Infrastructure**

**Fix "Permission Denied" (Linux)**
If Testcontainers fails with "Could not find valid Docker environment".

```bash
sudo chmod 666 /var/run/docker.sock

```

**Check Running Containers**
See if Postgres is spinning up during tests/app run.

```bash
docker ps

```

---

###  **API Quick Reference (cURL)**

**1. Register a User**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "andika", "password": "password123"}'

```

**2. Login (Basic Auth)**
*Note: Since we are using Basic Auth for now, you provide credentials in the header or URL.*

```bash
curl -u andika:password123 http://localhost:8080/api/exams

```

**3. Get All Exams (Public/User)**

```bash
curl -u andika:password123 http://localhost:8080/api/exams

```

