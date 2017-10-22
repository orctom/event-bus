package com.orctom;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class EventBusThreadFactory implements ThreadFactory {

  private static final AtomicInteger poolNumber = new AtomicInteger(1);
  private final ThreadGroup group;
  private final AtomicInteger threadNumber = new AtomicInteger(1);
  private final String namePrefix;

  EventBusThreadFactory() {
    SecurityManager s = System.getSecurityManager();
    group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    namePrefix = "event-bus-" + poolNumber.getAndIncrement() + "-thread-";
  }

  public Thread newThread(Runnable r) {
    Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
    t.setDaemon(true);
    return t;
  }
}
