package com.orctom;

import java.util.concurrent.TimeUnit;

public class Producer {

  private EventBus eventBus = EventBus.getInstance();

  public void start() throws InterruptedException {
    for (int i = 0; i < 20; i++) {
      System.out.println("sending " + i + " " + System.currentTimeMillis());
      eventBus.dispatch(DummyEvent.builder()
          .id("id-" + i)
          .value("value-" + i)
          .build());
      System.out.println("sent    " + i + " " + System.currentTimeMillis());
      TimeUnit.SECONDS.sleep(1);
    }
  }
}
