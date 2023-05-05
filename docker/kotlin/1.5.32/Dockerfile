FROM amazoncorretto:11
RUN set -eux \
    && cd / \
    && yum install -y unzip \
    && curl -sLO https://github.com/JetBrains/kotlin/releases/download/v1.5.32/kotlin-compiler-1.5.32.zip \
    && echo "2e728c43ee0bf819eae06630a4cbbc28ba2ed5b19a55ee0af96d2c0ab6b6c2a5 kotlin-compiler-1.5.32.zip" > sha256 \
    && sha256sum -c sha256 \
    && unzip kotlin-compiler-1.5.32.zip \
    && rm kotlin-compiler-1.5.32.zip sha256
ENV PATH="$PATH:/kotlinc/bin"