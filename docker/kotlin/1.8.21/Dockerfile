FROM amazoncorretto:11
RUN set -eux \
    && cd / \
    && yum install -y unzip \
    && curl -sLO https://github.com/JetBrains/kotlin/releases/download/v1.8.21/kotlin-compiler-1.8.21.zip \
    && echo "6e43c5569ad067492d04d92c28cdf8095673699d81ce460bd7270443297e8fd7 kotlin-compiler-1.8.21.zip" > sha256 \
    && sha256sum -c sha256 \
    && unzip kotlin-compiler-1.8.21.zip \
    && rm kotlin-compiler-1.8.21.zip sha256
ENV PATH="$PATH:/kotlinc/bin"