FROM amazoncorretto:11
RUN set -eux \
    && cd / \
    && yum install -y unzip \
    && curl -sLO https://archive.apache.org/dist/groovy/4.0.11/distribution/apache-groovy-binary-4.0.11.zip \
    && echo "335fe28bc7d93ab9f73e0d21bfc3591c63f4532572e7e1f93dc22cb0401ab422 apache-groovy-binary-4.0.11.zip" > sha256 \
    && sha256sum -c sha256 \
    && unzip apache-groovy-binary-4.0.11.zip \
    && rm apache-groovy-binary-4.0.11.zip sha256
ENV PATH="$PATH:/groovy-4.0.11/bin"
