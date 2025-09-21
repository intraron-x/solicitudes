FROM eclipse-temurin:21-jdk-alpine
# Agrega la línea para crear el usuario 'appuser'
RUN adduser -D -h /home/appuser -s /bin/sh appuser
VOLUME /tmp
COPY applications/app-service/build/libs/solicitudes.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
# Replace with a non-root user to avoid running the container with excessive privileges
USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar /app.jar" ]
