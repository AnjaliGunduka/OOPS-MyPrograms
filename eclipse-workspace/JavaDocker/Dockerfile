FROM openjdk
COPY src JavaDocker
WORKDIR JavaDocker
RUN mkdir -p bin
RUN javac -d bin -/com/example/docker/HelloWorld.java
WORKDIR bin
CMD ["java", "com.example.docker.HelloWorld"]