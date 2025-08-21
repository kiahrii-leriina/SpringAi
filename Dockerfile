FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
COPY wait-for-ollama.sh wait-for-ollama.sh
RUN chmod +x wait-for-ollama.sh


COPY wait-for-ollama.sh ./wait-for-ollama.sh
RUN chmod +x ./wait-for-ollama.sh

ENTRYPOINT ["./wait-for-ollama.sh", "http://ollama:11434", "java", "-jar", "app.jar"]
