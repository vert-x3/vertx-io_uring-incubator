package io.vertx.uring;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.spi.transport.Transport;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
}
