# README

This document provides an overview of the project, setup instructions, and other important details.

## Project Overview

- **Project Name**: `edufinder`
- **Backend Domain**: [http://localhost:8080](http://localhost:8080)
- **Project Description**:

    This project was developed as part of the **2006-SCSH-90** group assignment. It provides a platform for searching educational institutions in Singapore, using data sourced from the official [data.gov.sg](https://data.gov.sg/)
    datasets. This repository only contains the backend code I developed for the project.

---

## Role & Contribution
This was a **group academic project**. My contributions include:

- Designing and implementing the **backend architecture**
- Building **REST APIs** for frontend consumption
- Implementing **authentication and authorization**
- Handling **database schema, queries, and data processing**
- Writing **unit tests** and backend documentation

Frontend and UI were implemented by other team members.

---

## Technologies

- **Language & Framework**: Java, Spring Boot
- **Database**: MySQL
- **Testing**: JUnit, Mockito
- **Tools**: Maven, Git

---

## API Endpoints
Full API documentation is in [API Reference](./docs/API_REFERENCE.md).

---

## Installation and Setup

### Prerequisites

Ensure the following tools are installed and properly configured:
- [git](https://docs.github.com/en/get-started/git-basics/set-up-git)

    Verify installation:
    ```bash
    git -v
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
git https://github.com/Yong106/Edufinder-SC2006
cd Edufinder-SC2006
```

---

### Database Setup
The project requires a local **MySQL** server running on **port 3306**. 
Setup using either XAMPP or MySQL.

**XAMPP (Recommended)**

For the easiest setup, install **XAMPP** and enable the **MySQL** service.
1. Start **XAMPP** (**Run as administrator** if using **Windows**). 
2. Start **MySQL** and **Apache Web Server** from the XAMPP Control Panel.
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

```bash
cd backend/edufinder
mvn spring-boot:run
```
To stop, press:
```
Ctrl + C
```

---

## Folder Structure

**Project**
```
2006-SCSH-90/
├── backend/               # Backend
├── docs/                  # Other documentation
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
- [JavaDoc](./docs/javadoc)

## Notes
- This repository is shared for **portfolio purposes** only.
- All code is my own contribution to the project’s backend.