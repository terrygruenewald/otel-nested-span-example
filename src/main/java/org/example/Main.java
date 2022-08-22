package org.example;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.jaeger.thrift.JaegerThriftSpanExporter;
import io.opentelemetry.exporter.jaeger.thrift.JaegerThriftSpanExporterBuilder;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

public class Main {
  public static void main(String[] args) {

    System.out.println("Starting OpenTelemetry example for nested spans");
    io.opentelemetry.sdk.resources.Resource resource = io.opentelemetry.sdk.resources.Resource.getDefault()
      .merge(io.opentelemetry.sdk.resources.Resource.create(io.opentelemetry.api.common.Attributes.of(AttributeKey.stringKey("service.name"), "logical-service-name")));

    JaegerThriftSpanExporterBuilder jaegerExporter = JaegerThriftSpanExporter.builder();

    io.opentelemetry.sdk.trace.SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
      .addSpanProcessor(SimpleSpanProcessor.create(jaegerExporter.build()))
      .setResource(
        Resource.getDefault().toBuilder()
          .put(AttributeKey.stringKey("service.name"), "my-logical-service-name")
          .build())
      .build();


    OpenTelemetry openTelemetry =
      OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
        .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
        .build();
    foo(openTelemetry);

    System.out.println("Done!");

  }

  private static void foo(OpenTelemetry openTelemetry) {
    Tracer tracer =
      openTelemetry.getTracer("instrumentation-library-name", "1.0.0");
    Span span = tracer.spanBuilder("in-foo").startSpan();

    // Make the span the current span
    try (Scope ss = span.makeCurrent()) {
      // In this scope, the span is the current/active span
      bar(tracer);
    } finally {
      span.end();
    }
  }

    private static void bar(Tracer tracer) {

      Span childSpan = tracer.spanBuilder("in-bar-child-lisa")
        .startSpan();

      try (Scope scope = childSpan.makeCurrent()) {
        System.out.println("In bar");
      } finally {
        childSpan.end();
      }
    }

}