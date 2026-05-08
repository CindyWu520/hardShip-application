# Build stage
FROM public.ecr.aws/docker/library/maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app

# ✅ Cache dependencies — only re-downloads when pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -B

# ✅ Then copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-headless
WORKDIR /app

# ✅ Copy only the JAR from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]