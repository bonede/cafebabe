# Cafebabe 
Visual disassembler for JVM based languages

![image info](https://raw.githubusercontent.com/bonede/cafebabe/main/assets/screenshot.png)

# Use it online

Please visit https://www.cafebabe.app

# System requirements:
* JDK 11+
* Docker
* Redis

# Run it in you local box
```console
$ ./gradlew bootRun
# Open http://localhost:8080/ in browser
```
# Run it using Docker
```console
$ docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
  -p 8080:8080 \
  -e SPRING_REDIS_HOST={redis.host.name} \
  -e SPRING_REDIS_PORT={redis.port} \
  -v /data/cafebabe:/data/cafebabe \
  bonede/cafebabe:0.0.1
# Open http://localhost:8080/ in browser
```


# Build a release
```console
# Build frontend
$ cd cafebabe-ui && npm run build
# Build Java webapp
$ ./gradlew bootJar
```

# Start development
```console
# Start webapp
$ ./gradlew bootRun
# Start frontend
$ cd cafebabe-ui && npm run dev
# Open http://localhost:5173/ in browser
```






