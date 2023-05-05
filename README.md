# CafeBabe 
Visual disassembler for JVM based languages

# Use it online

Please visit https://www.cafebabe.app

# System requirements:
* JDK 11+
* Docker
* Redis

# Run it in you local box
```shell
cd cafebabe-web && ./gradlew bootJar
# Open http://localhost:8080/ in browser
```
# Run it using Docker
```shell
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
  -p 8080:8080 \
  -e SPRING_REDIS_HOST=redis.host.name \
  -e SPRING_REDIS_PORT=6379 \
  -v /data/cafebabe:/data/cafebabe \
  bonede/cafebabe:0.0.1
# Open http://localhost:8080/ in browser
```


# Build a release
```shell
# Build frontend
cd cafebabe-ui && npm run build
# Build Java webapp
cd cafebabe-web && ./gradlew bootJar
```

# Start development
```shell
# Start webapp
cd cafebabe-ui && ./gradlew bootJar
# Start frontend
cd cafebabe-ui && npm run dev
# Open http://localhost:5173/ in browser
```





