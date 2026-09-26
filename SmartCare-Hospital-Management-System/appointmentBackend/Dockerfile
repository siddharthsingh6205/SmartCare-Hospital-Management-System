
FROM maven:3.9.9-eclipse-temurin-25 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

#Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

#Use an official Java 25 runtime image to run the application
FROM eclipse-temurin:25-jdk

#Set the working directory
WORKDIR /app

#Copy the built JAR file from the build stage
COPY --from=build /app/target/appointmentBackend-0.0.1-SNAPSHOT.jar .

# Expose port 8080
EXPOSE 8080

#Specify the command to run the application
ENTRYPOINT ["java","-jar","/app/appointmentBackend-0.0.1-SNAPSHOT.jar"]

