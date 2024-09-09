# Flight Search Application
This application allows you to perform request to the Amadeus API, searching and showing several flight results for a given search.

## Requirements
Docker
Docker Compose

## Backend
The backend of the project is in charge of communicating with the Amadeus API, is located in the backend directory and is built using Spring Boot with Gradle.

## Frontend
The fronted communicates with the backend, sending and receiving the information to display the results, is located in the frontend directory and built using React with TypeScript.

## Geting started
1. Clone the repository:
   
```bash
git clone https://github.com/JorgeSanchez-Encora/FlightSearch.git
```

2. Change to the project directory:
   
```bash
cd FlightSearch
```

3. Add your Amadeus credentials to the backend application.properties file

```bash
apiKey = <Your api key>
apiSecret = <Your api secret>
```

## Build and Run using Docker Compose
1. Build and start both services

```bash
docker-compose up --buld
```

2. In case that you have already ran the build command, you can use

```bash
docker-compose up
```

3. To turn down both services use

```bash
docker-compose down
```
4. To acces the aplication, go to
- Frontend: http://localhost:3000
- Backend: http://localhost:8080

## Runing the app without docker

### Backend
#### Prerequisites
- Java 22
- Spring Boot 3.3.3
- Gradle 8.8

#### Bulding and running the application
1. First, change to the backend directory
```bash
cd backend
```
2. Second, build the .jar file using
 ```bash
 ./gradlew build
 ```
3. Then, run the app using
```bash
./gradlew bootRun
```

### Frontend
#### Prerequisites
- Node 22
- npm 10

#### Building and running the application
1. First, change to the frontend directory
```bash
cd frontend
```
2. Second, install the dependencies
 ```bash
 npm install
 ```
3. Then, run the app using
```bash
npm start
```




