FROM amazoncorretto:11
RUN set -eux \
    && cd / \
    &&  yum install -y unzip \
    && curl -sLO https://archive.apache.org/dist/groovy/2.5.22/distribution/apache-groovy-binary-2.5.22.zip \
    && echo "622db9bb68b51e9f319cb78dd634e172ba486a72c60115d4b62a171b9679373e apache-groovy-binary-2.5.22.zip" > sha256 \
    && sha256sum -c sha256 \
    && unzip apache-groovy-binary-2.5.22.zip \
    && rm apache-groovy-binary-2.5.22.zip sha256
ENV PATH="$PATH:/groovy-2.5.22/bin"
