FROM amazoncorretto:11
RUN set -eux \
    && cd / \
    &&  yum install -y unzip \
    && curl -sLO https://github.com/JetBrains/kotlin/releases/download/v1.7.21/kotlin-compiler-1.7.21.zip \
    && echo "8412b31b808755f0c0d336dbb8c8443fa239bf32ddb3cdb81b305b25f0ad279e kotlin-compiler-1.7.21.zip" > sha256 \
    && sha256sum -c sha256 \
    && unzip kotlin-compiler-1.7.21.zip \
    && rm kotlin-compiler-1.7.21.zip sha256
ENV PATH="$PATH:/kotlinc/bin"