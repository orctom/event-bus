package com.orctom;

import java.util.concurrent.TimeUnit;

public class Consumers {

  private Consumer current;
  private Consumer preivous = null;

  public void start() throws InterruptedException {
    current = new Consumer("jim");
    current.start();
    TimeUnit.SECONDS.sleep(5);
    preivous = current;
    current = new Consumer("bob");
    current.start();
    TimeUnit.SECONDS.sleep(2);
    preivous.stop();
    preivous = null;
    System.gc();
    TimeUnit.SECONDS.sleep(10);
  }

  public static void main(String... args) throws Exception {
    new Thread(() -> {
      try {
        new Consumers().start();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
    TimeUnit.SECONDS.sleep(1);
    new Thread(() -> {
      try {
        new Producer().start();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
    TimeUnit.SECONDS.sleep(30);
  }
}
