FROM amazoncorretto:11
RUN set -eux \
    && amazon-linux-extras install docker
COPY build/libs/cafebabe-web.jar /jar/
ENTRYPOINT ["java", "-jar", "/jar/cafebabe-web.jar"]
