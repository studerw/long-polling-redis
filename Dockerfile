FROM tomcat:8.5
COPY ./target/long-polling-redis-2.0-SNAPSHOT.war /usr/local/tomcat/webapps/long-polling-redis.war
EXPOSE 8080 8443
