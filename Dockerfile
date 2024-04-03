FROM  openjdk:17-jdk
ENV java_opts="--add-opens=java.base/java.nio=ALL-UNNAMED"
ENV java_args=""
COPY target/backend-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT exec java $java_opts -jar app.jar $java_args
EXPOSE 8081