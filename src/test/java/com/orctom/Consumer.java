package com.orctom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Consumer {

  private EventBus eventBus = EventBus.getInstance();

  private EventListener<DummyEvent> onResultListener = this::onResult;

  private String id;

  public Consumer(String id) {
    this.id = id;
  }

  private void onResult(DummyEvent event) {
    log.debug("{} received: {}", id, event);
  }

  public void start() {
    eventBus.addEventListener(DummyEvent.class, onResultListener);
  }

  public void stop() {
    eventBus.removeEventListener(DummyEvent.class, onResultListener);
  }

  @Override
  public String toString() {
    return "Consumer-" + id;
  }
}
