FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jre-11.0.18_10

COPY mit.jar /app/
COPY time.jar /app/

WORKDIR /app

CMD java -jar mit.jar
/ --plugin 'time.jar!org.renaissance.plugins.jmxtimers.Main'
/ --json 1-finagle-chirper-time.json finagle-chirper

VOLUME /app


# to run the container execute following run command for seccomp and apparmor profiles
# or just remove themif you need plain docker
# docker run --security-opt seccomp=/path/to/profile.json --security-opt apparmor=docker-default -v /path/to/local/directory:/app <container name>