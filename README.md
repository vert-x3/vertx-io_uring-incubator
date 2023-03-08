# Vert.x io_uring Incubator

[![Build Status (5.x)](https://github.com/vert-x3/vertx-io_uring-incubator/actions/workflows/ci-5.x.yml/badge.svg)](https://github.com/vert-x3/vertx-io_uring-incubator/actions/workflows/ci-5.x.yml)
[![Build Status (4.x)](https://github.com/vert-x3/vertx-io_uring-incubator/actions/workflows/ci-4.x.yml/badge.svg)](https://github.com/vert-x3/vertx-io_uring-incubator/actions/workflows/ci-4.x.yml)

Vert.x io_uring is a transport using the io_uring interface of the Linux kernel.

Vert.x io_uring works with

- [Vert.x Core](https://vertx.io/docs/vertx-core/java/) TCP, HTTP, datagram servers/clients
- [Vert.x gRPC](https://vertx.io/docs/vertx-grpc/java/)
- [PostgeSQL](https://vertx.io/docs/vertx-pg-client/java/), [MySQL](https://vertx.io/docs/vertx-mysql-client/java/), [MSSQL](https://vertx.io/docs/vertx-mssql-client/java/) and [DB2](https://vertx.io/docs/vertx-db2-client/java/) reactive clients
- [Redis](https://vertx.io/docs/vertx-redis-client/java/) client
- [Vert.x MQTT](https://vertx.io/docs/vertx-mqtt/java/)
- [Vert.x AMQP](https://vertx.io/docs/vertx-amqp-client/java/) client
- [Vert.x STOMP](https://vertx.io/docs/vertx-stomp/java/)
- [Vert.x HTTP Proxy](https://vertx.io/docs/vertx-http-proxy/java/)
- [Vert.x Mail](https://vertx.io/docs/vertx-mail-client/java/)
- [Vert.x Consul](https://vertx.io/docs/vertx-consul-client/java/) client

WARNING: This module has _incubation_ status and only works on some Linux OSes.

## Prerequisites

- [Vert.x 4.4.0](https://vertx.io/docs/4.4.0)

## Usage

Snapshots are available at s01.oss.sonatype.org

```xml
<repository>
  <id>sonatype-nexus-snapshots</id>
  <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
  <layout>default</layout>
  <releases>
    <enabled>false</enabled>
  </releases>
</repository>
```

