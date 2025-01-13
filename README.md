# KursDA!

### Comparison platform of education providers in Germany

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gula1507_weiterbildungsfinder-backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=gula1507_weiterbildungsfinder-backend) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=gula1507_weiterbildungsfinder-backend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=gula1507_weiterbildungsfinder-backend) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=gula1507_weiterbildungsfinder-backend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=gula1507_weiterbildungsfinder-backend) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=gula1507_weiterbildungsfinder-backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=gula1507_weiterbildungsfinder-backend) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=gula1507_weiterbildungsfinder-backend&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=gula1507_weiterbildungsfinder-backend)

## README in Other Languages

- [README in German](README_DE.md)

## Description

The main goal of the project is to make it easier for users to search for a continuing education provider, enable them
to leave reviews for course providers, and list all existing continuing education providers in Germany.

## Features

Die Anwendung bietet folgende Möglichkeiten:

- **User registration and login** mit Spring Security
- **CRUD operations** for continuing education providers
- fetch additional data about course providers from **external API**
- Listing of over 200 education providers
- **Search function** by course provider name
- Ability to **leave a review**, including star rating and comment
- Modern user interface with **React**, including a responsive design

## Technology Stack

### Backend

- Java (Spring Boot, Spring Security)
- MongoDB with MongoRepository
- Docker for containerization
- RESTful APIs

### Frontend

- React
- TypeScript
- Axios for API-calls
- HTML/CSS3

### Testing

- JUnit 5
- Mockito
- **Flapdoodle** for embedded MongoDB in tests

### Other Technologies

- SonarCloud

## Deployment

The project is available at the following URL: [https://kursda.onrender.com/](https://kursda.onrender.com/).

**Note:** The page may take a little longer to load. Please be patient.

## Projekt-Setup

### Prerequisites

Make sure you have the following software installed:

- **Java 17+** for the backend
- **Node.js 16+** for the frontend
- **MongoDB**

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/Gula1507/weiterbildungsfinder.git
cd weiterbildungsfinder
```

### 2. Start the backend

Navigate to the backend directory and start the Spring Boot application:

```bash
cd backend
./mvnw spring-boot:run
```

### 3. Start the frontend

Navigate to the frontend directory, install the dependencies, and start the application:

```bash
cd frontend
npm install
npm start
```

### Configuration

To set environment variables, create a .env file in the backend directory and add the following values:

```bash
SPRING_MONGODB_URI=mongodb://localhost:27017/your-database
```

### Screenshots

Screenshot of the homepage:

<img src="frontend/public/images/screenshot2.png" alt="Dashboard screenshot"  style="max-width:80%; height: auto;">

Screenshot of the course provider detail page:

<img src="frontend/public/images/screenshot1.png" alt="Course provider screenshot" style="max-width:80%; height: auto;">

### Planned Improvements

- Extension of the filter and search function for course providers
- Extended login/logout functionality
- CRUD operations for courses
- Login with LinkedIn

### License

This project is licensed under the MIT license with a clause for non-commercial use. See the [LICENSE file](./LICENSE)
for details.

