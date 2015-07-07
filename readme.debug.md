To Debug the web app using Maven, Tomcat7, and Eclipse:

From Command Line:
    Linux / Cygwin:
    export MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=4001,server=y,suspend=n -XX:MaxPermSize=512m -Xms1024m -Xmx2048m"

    Windows:
    set MAVEN_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=4001,server=y,suspend=n -XX:MaxPermSize=512m -Xms1024m -Xmx2048m

    mvn jetty:run OR mvn tomcat7:run

    At the very beginning of the Tomcat/Jetty output, you should see the following line:
    'Listening for transport dt_socket at address: 4001'
