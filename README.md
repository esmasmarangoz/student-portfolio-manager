# Student Portfolio Manager

A full-stack Java web application that allows students to manage their profiles and administrators to control a centralized student directory system.

---

## Project Overview

This project is a web-based system built using Java Servlets without any frameworks. It demonstrates core web development concepts such as authentication, session management, CRUD operations, and client-server communication using AJAX.

The application follows a clean structure where the frontend (HTML/CSS/JavaScript) communicates with backend Servlets, which interact with a relational database.

---

## Features

### Authentication
- User registration
- Login and logout functionality
- Session-based authentication

### Student Functionality
- View personal profile
- Edit profile information
- Dynamic profile updates

### Admin Panel
- View all students
- Add new students
- Edit student profiles
- Delete students
- Full system control restricted to admin users

### Filtering and Search
- Search students by name or skill
- Filter students by department
- Automatically updates department list based on database content

---

## Technologies Used

- Java (Servlets, JDBC)
- Apache Tomcat 9
- SQLite (local database)
- HTML5, CSS3
- JavaScript (Fetch API / AJAX)
- Maven

---

## Project Structure

```
src/
 ├── main/
 │   ├── java/com/esma/webtech/
 │   │   ├── servlet/
 │   │   ├── util/
 │   │   └── model/
 │   ├── webapp/
 │   │   ├── WEB-INF/web.xml
 │   │   ├── index.html
 │   │   ├── login.html
 │   │   ├── register.html
 │   │   ├── profile.html
 │   │   ├── admin.html
```

---

## Setup Instructions

- Clone the repository

```
git clone https://github.com/YOUR_USERNAME/student-portfolio-manager.git
```

- Build the project

```
mvn clean package
```

- Deploy the WAR file to

```
tomcat/webapps/
```

- Open in browser

```
http://localhost:8080/student-portfolio-manager
```

---

## Default Accounts

### Admin
- username: admin
- password: admin123

### Student
- username: esma
- password: esma123

---

## Key Points

- Built without frameworks to demonstrate core Java knowledge
- Full CRUD system implemented
- Session-based authentication
- Clean frontend-backend separation
- Admin control panel included

---

## Future Improvements

- Password hashing
- Profile image upload
- UI improvements
- Pagination
- REST API expansion

---

## Author

Esma Marangoz  
Computer Engineering Student
