FROM openjdk:17
LABEL stage=forbuild
COPY ./target/project.war /project.war
WORKDIR /
EXPOSE 8000
ENTRYPOINT [ "java", "-jar", "/project.war" ]