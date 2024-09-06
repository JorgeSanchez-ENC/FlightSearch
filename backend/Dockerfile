
FROM eclipse-temurin:22 AS builder

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean build -x test

FROM eclipse-temurin:22

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]