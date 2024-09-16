package io.vertx.uring;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.spi.transport.Transport;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static io.vertx.core.http.HttpMethod.PUT;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleTest {

  @Test
  public void testTransport() {
    Vertx vertx = Vertx.vertx(new VertxOptions().setPreferNativeTransport(true));
    try {
      Transport transport = ((VertxInternal) vertx).transport();
      assertTrue("Expected " + IOUringTransport.class.getName() + " instead of " + transport.getClass().getName(), transport instanceof IOUringTransport);
    } finally {
      vertx.close();
    }
  }

  @Test
  public void testReproducer() throws Exception {

    Vertx vertx = Vertx.vertx(new VertxOptions().setPreferNativeTransport(true));

    HttpClient client = vertx.createHttpClient(new HttpClientOptions()
      .setKeepAlive(true)
      .setMaxPoolSize(1)
    );

    HttpServer server = vertx.createHttpServer();

    AtomicInteger reqCount = new AtomicInteger(0);
    server.connectionHandler(conn -> {
      conn.exceptionHandler(err -> {
        log.accept("Connection error "  + err.getMessage());
      });
      conn.closeHandler(v -> {
        log.accept("Connection closed");
      });
    });
    server.requestHandler(req -> {
      int theCount = reqCount.get();
      reqCount.incrementAndGet();
      log.accept("Got request " + theCount);
      req.body()
        .onSuccess(buff -> {
          log.accept("Got body " + theCount);
          // The bug does not happen when we remove the timer and directly send the response
          vertx.setTimer(1 + (long) (10 * Math.random()), id -> {
            log.accept("Send response " + theCount);
            req.response().headers().set("count", String.valueOf(theCount));
            req
              .response()
              .end(buff)
              .onComplete(ar -> {
              log.accept("Sent response " + theCount + ar.succeeded());
            });
          });
        });
    });

    CountDownLatch latch = new CountDownLatch(requests);
    server.listen().toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);
    doRequest(client, latch);

    if (!latch.await(10, TimeUnit.SECONDS)) {
      log.accept("Timeout");
      fail();
    }
  }

  final int requests = 100;
  final long now = System.currentTimeMillis();
  Consumer<String> log = msg -> {
    long time = (System.currentTimeMillis() - now) / 10;
    System.out.println(msg + " (" + time / 100F + ")");
  };

  AtomicInteger count = new AtomicInteger();

  private void doRequest(HttpClient client, CountDownLatch latch) {
    int theCount = count.getAndIncrement();
    if (theCount >= requests) {
      return;
    }
    log.accept("Want request " + theCount);
    RequestOptions options = new RequestOptions().setHost("localhost").setPort(8080).setMethod(PUT);
    client.request(options)
      .onSuccess(req -> {
        req.putHeader("count", String.valueOf(theCount));
        req.send(Buffer.buffer("This is content " + theCount)).onSuccess(resp -> {
          log.accept("Got response " + theCount);
          resp.body().onSuccess(buff -> {
            log.accept("Got response body " + theCount);
            latch.countDown();
            doRequest(client, latch);
          });
        });
      });
  }
}
