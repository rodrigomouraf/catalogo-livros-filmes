FROM tomcat:10.1-jdk21-temurin

RUN apt-get update && apt-get install -y default-mysql-client

RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/catalogo-livros-filmes.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
