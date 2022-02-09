FROM openjdk:11-jdk-slim
COPY "build/libs/java-explorer-compiler.jar" "/opt/jars"
EXPOSE 8080
CMD ["java", "-jar", "/opt/jars/java-explore-compiler.jar"]