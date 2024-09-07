FROM openjdk:21
# Create a group and user
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring
ARG JAR_FILE=../build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
