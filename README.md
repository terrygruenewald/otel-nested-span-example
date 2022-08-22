# otel-nested-span-example

This demonstrates how to do nested spans.  I was having difficulty with this so I created a simple example.  This example works.  To run it:
```
$ mvn package
$ cd target
$ java -jar otel-nested-span-example-1.0-SNAPSHOT.jar
```
Before running you will need to have this Docker container running:
```
docker run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
  -e COLLECTOR_OTLP_ENABLED=true \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 4317:4317 \
  -p 4318:4318 \
  -p 14250:14250 \
  -p 14268:14268 \
  -p 14269:14269 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.37
```
(from https://www.jaegertracing.io/docs/1.37/getting-started/ )
