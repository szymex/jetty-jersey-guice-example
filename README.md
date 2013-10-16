EmbeddedJetty-Jersey-Guice-Example
==========================

Hello world example project that integrates:
- Jetty embedded 9.x
- Jersey 1.x
- Guice 3.0

In addition it uses simple filter, custom provider and asynchronous requests.

## Usage

`mvn exec:java -Dexec.args="pl"`
(for polish hello world)

`mvn exec:java -Dexec.args="fi"`
(for finnish hello world)

http://localhost:8080/hello.json

http://localhost:8080/hello
(Will return 403 Forbidden)

http://localhost:8080/hello?user=bob

http://localhost:8080/async-hello?d=2
(Will return after 2 seconds)

