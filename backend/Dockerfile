FROM eclipse-temurin:22
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]