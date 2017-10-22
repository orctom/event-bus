package com.orctom;

public interface EventListener<T> {

  void onMessage(T t);

}
