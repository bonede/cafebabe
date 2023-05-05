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






