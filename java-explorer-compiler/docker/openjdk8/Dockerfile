FROM openjdk:8-jdk-slim
COPY "build/libs/java-explorer-compiler.jar" "/opt/jars/"
EXPOSE 8080
CMD ["java", "-jar", "/opt/jars/java-explorer-compiler.jar", "--compiler.nickname=openjdk8"]