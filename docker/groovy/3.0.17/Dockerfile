FROM amazoncorretto:11
RUN set -eux \
    && cd / \
    && yum install -y unzip \
    && curl -sLO https://archive.apache.org/dist/groovy/3.0.17/distribution/apache-groovy-binary-3.0.17.zip \
    && echo "efcfb9f98f516ef46419a3888f27d61711e8de62488f12ec921a1189833062db apache-groovy-binary-3.0.17.zip" > sha256 \
    && sha256sum -c sha256 \
    && unzip apache-groovy-binary-3.0.17.zip \
    && rm apache-groovy-binary-3.0.17.zip sha256
ENV PATH="$PATH:/groovy-3.0.17/bin"
