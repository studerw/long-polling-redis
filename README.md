*Note that if the application is not shutdown correctly (happens on Cygwin with Tomcat7 and Maven), the embedded
Redis server will not be stopped. The application will not be able to start again due to the port
binding, and a large Redis temporary file will take up space in your home directory. Look in the process explorer for an app called `redis-server-x.y.x` and kill it.*


# long-polling-redis
Example App showing client side long polling using Spring MVC and Redis Pub/Sub.

Uses Spring 4 MVC, Spring Data Redis, JQuery on the front-end, Tomcat 8, and Redis 4.

### Overview

A naive approach to front-end querying of the backend for new emails, messages, alerts, etc. is to poll every *x* seconds.
In Javascript, this is usually done using the `setInterval()` function.

For example, if one were to design a email app similar to GMail, the user will expect new incoming
messages to be shown in the inbox count automatically (i.e. without having to constantly refresh the page).

Polling again and again is a waste of resources on both the client and server, especially when the frequency
of polling is far greater than new incoming messages are received.

A better way to approach the problem is by using async requests on a pubsub channel on the backend, along
with a recursive function on the front-end that only makes new requests when the old has actually returned.


### Building

From the command line, run:

````
$ mvn clean install
$ docker build -t "long-polling-redis" .
$ docker-compose up
````

Open your browser to [http://127.0.0.1:8080/long-polling-redis](http://127.0.0.1:8080/long-polling-redis).

In the input box, add messages.

The left column shows the output from a front-end approach using async requests on the server and a recursive
function on the front-end to re-call itself upon completion / timeout of each request. Requests on the back-end
will simply block (though without holding the request thread) until Redis informs the waiting requests that a new message
has been added.

The right column shows the traditional attempt with polling. Every 10 seconds (configurable in `app.properties` file),
the front-end will make a call to the server requesting new messages. The server responds immediately with the result.
If messages are created less frequently than the poll time, most of the requests are wasted.
