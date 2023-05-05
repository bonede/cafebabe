FROM amazoncorretto:11
RUN set -eux \
    && cd / \
    && yum install -y unzip \
    && curl -sLO https://archive.apache.org/dist/groovy/2.4.21/distribution/apache-groovy-binary-2.4.21.zip \
    && echo "2f0f59aeaa054a241705b102111a2d823c19ee0f840a6c5bb3e2d26907058f40 apache-groovy-binary-2.4.21.zip" > sha256 \
    && sha256sum -c sha256 \
    && unzip apache-groovy-binary-2.4.21.zip \
    && rm apache-groovy-binary-2.4.21.zip sha256
ENV PATH="$PATH:/groovy-2.4.21/bin"
