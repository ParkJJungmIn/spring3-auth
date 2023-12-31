FROM --platform=linux/amd64 openjdk:17-alpine

WORKDIR /usr/src/app

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/start-spring-3-0.0.1-SNAPSHOT.jar ${JAR_PATH}/start-spring-3-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","./build/libs/start-spring-3-0.0.1-SNAPSHOT.jar"]