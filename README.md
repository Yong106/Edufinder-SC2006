# README

This document provides an overview of the project, setup instructions, and other important details.

## Project Overview

- **Project Name**: `edufinder`
- **Frontend Domain**: [http://localhost:5173](http://localhost:5173)
- **Backend Domain**: [http://localhost:8080](http://localhost:8080)
- **Project Description**:

    This project was developed as part of the **2006-SCSH-90** group assignment. It provides a platform for searching educational institutions in Singapore, using data sourced from the official [data.gov.sg](https://data.gov.sg/)
    datasets.

---

## Installation and Setup

### Prerequisites

Ensure the following tools are installed and properly configured:
- [git](https://docs.github.com/en/get-started/git-basics/set-up-git)

    Verify installation:
    ```bash
    git -v
    ```

- [Node.js and npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)

    Verify installation:
    ```bash
    node -v
    npm -v
    ```
  
- [JDK 22](https://www.oracle.com/asean/java/technologies/downloads/)

    Verify installation:
    ```bash
    java -version
    ```
  
- [maven](https://maven.apache.org/install.html)

    Verify installation:
    ```bash
    mvn -v
    ```
  
- [XAMPP](https://www.apachefriends.org/) or [MySQL](https://dev.mysql.com/doc/refman/8.0/en/installing.html)

### Clone the Repository

```bash
git clone https://github.com/softwarelab3/2006-SCSH-90.git
cd 2006-SCSH-90
```

---

### Database Setup
The project requires a local **MySQL** server running on **port 3306**. 
Setup using either XAMPP or MySQL.

**XAMPP (Recommended)**

For the easiest setup, install **XAMPP** and enable the **MySQL** service.
1. Start **XAMPP** (**Run as administrator** if using **Windows**). 
2. Start **MySQL** from the XAMPP Control Panel.
3. Ensure it is running on port **3306**.

Populate the initial data:
1. Copy the queries in [edufinder.sql](./setup/database/edufinder.sql).
2. Open phpMyAdmin at: [http://localhost/phpmyadmin/](http://localhost/phpmyadmin/).
3. Go to the **SQL** tab.
4. Paste the SQL script and click **Go**.

If XAMPP’s MySQL uses a different port, update the configuration.

---

**MySQL**

Different distributions of MySQL may use slightly different commands.
Always verify the installed version:
```bash
mysql --version
```

**Start MySQL**:

**Windows**
```bash
net start MySQL
```

**macOS (Homebrew)**
```bash
brew services start mysql
```

Check the active port:
```bash
lsof -iTCP -sTCP:LISTEN | grep mysqld
```

If MySQL is not listening on **3306**, update your MySQL configuration file (my.cnf or my.ini) and restart the service.

**Populate the initial data**:

Once the MySQL server is running on the correct port, you can import the schema using:
```bash
mysql -u root -p -h 127.0.0.1 -P 3306 < ./setup/database/edufinder.sql && echo "Database Populated"
```
When prompted for a password, enter the MySQL root password.
For fresh installations, the password may be empty unless configured.

---

### Install Dependencies

You may install all dependencies using the provided scripts, or perform the setup manually.

**Using Script**

**Windows (Powershell)**
```bash
.\setup\dependencies\setup.ps1
```

**Windows (Command Prompt)**
```bash
powershell -ExecutionPolicy Bypass -File ".\setup\dependencies\setup.ps1"
```

**macOS/Linux**
```bash
chmod +x ./setup/dependencies/setup.sh
./setup/dependencies/setup.sh
```

---

**Manual Setup**
```bash
cd backend/edufinder
mvn clean install

cd ../../frontend
npm install

cd ..
```

**Note**

This project does **not** support Java **25**.
If your build fails, verify the installed version:
```bash
mvn -version
```

If the Java version is not **22**, update your environment:
```bash
export JAVA_HOME=/path/to/java22
```
Replace `/path/to/java22` with the actual installation directory of JDK 22.

---

## Execution

Startup may take up to 2 minutes depending on your system and network.

**Backend**
```bash
cd backend/edufinder
mvn spring-boot:run
```
To stop the backend, press:
```
Ctrl + C
```

**Frontend**
```bash
cd frontend
npm run dev
```
To stop the frontend, press:
```
Ctrl + C
```

---

## Folder Structure

**Project**
```
2006-SCSH-90/
├── backend/               # Backend
├── docs/                  # Other documentation, including analysis model and lab deliverables
├── frontend/              # Frontend
├── setup/                 # Setup script and database schema
├── .gitignore
└── README.md
```

[**Backend(Main)**](./backend/edufinder/src/main/java/com/sc2006/g5/edufinder)
```
edufinder/
├── config/                # Config classes (e.g., cors, security, app, etc.)
├── controller/            # RestController classes, handles API request and response
├── dto/                   # Data Transfers Object classes, encapsulates data of API request and response
├── exception/             # Exception classes
├── mapper/                # Mapper classes, maps between entity and DTO
├── model/                 # Entity classes, encapsulates data used in application
├── repository/            # Repository classes, retrieves and manages data
├── security/              # Security classes (e.g., PasswordEncoder, SessionManager, etc.)
├── service/               # Service classes, handles business logic
├── util/                  # Util classes
└── EduFinderApplication   # Entry point of the application
```

[**Backend(Test)**](./backend/edufinder/src/test/java/com/sc2006/g5/edufinder)
```
edufinder/
├── integration/           # Integration test classes
└── unit/                  # Unit test classes
```

---

## Other Documentations
Detailed documentation is available in the [docs](./docs) directory.

Key documents include:

- [API Reference](./docs/API_REFERENCE.md)
- [Test Results](./docs/UnitTestResults.html)
- [Lab Deliverables](./docs/lab-delivarable)
- [Analysis Models](./docs/analysis-model)
- [JavaDoc](./docs/javadoc)

---

## Contributors

- **Lim Kiat Yang Ryan** (U2421937D)
- **Peng Sizhe** (U2423895H)
- **Tarun Ilangovan** (U2422251A)
- **Yong Chee Seng** (U2420563K)
- **Yu Wenhao** (U2421425F)