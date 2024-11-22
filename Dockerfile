FROM eclipse-temurin:23
LABEL maintainer="gulya@gmx.net"
EXPOSE 8080
COPY backend/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]