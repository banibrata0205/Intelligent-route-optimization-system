FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-Xms512m -Xmx2048m"

ENTRYPOINT ["java","-jar","app.jar"]
