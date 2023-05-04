FROM amazoncorretto:11
RUN set -eux \
    && cd / \
    &&  yum install -y unzip \
    && curl -sLO https://github.com/JetBrains/kotlin/releases/download/v1.6.21/kotlin-compiler-1.6.21.zip \
    && echo "632166fed89f3f430482f5aa07f2e20b923b72ef688c8f5a7df3aa1502c6d8ba kotlin-compiler-1.6.21.zip" > sha256 \
    && sha256sum -c sha256 \
    && unzip kotlin-compiler-1.6.21.zip \
    && rm kotlin-compiler-1.6.21.zip sha256
ENV PATH="$PATH:/kotlinc/bin"