FROM alpine AS prep
WORKDIR /prep
COPY build/libs/ .
RUN find . -name "*.jar" ! -name "*plain*" -exec cp {} /app.jar \;

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=prep /app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
