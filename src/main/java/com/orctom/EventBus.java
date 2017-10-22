package com.orctom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class EventBus {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);

  private static final EventBus INSTANCE = new EventBus();

  private ExecutorService workers = Executors.newFixedThreadPool(2, new EventBusThreadFactory());

  private Map<Class<?>, List<WeakReference<EventListener<?>>>> registry = new HashMap<>();

  private EventBus() {
  }

  public static EventBus getInstance() {
    return INSTANCE;
  }

  public <T> boolean addEventListener(Class<T> eventType, EventListener<T> eventListener) {
    LOGGER.debug("Adding event listener, event: {}, listener {}", eventType, eventListener);
    return registry.computeIfAbsent(eventType, key -> new ArrayList<>()).add(new WeakReference<>(eventListener));
  }

  public <T> boolean removeEventListener(Class<T> eventType, EventListener<T> eventListener) {
    LOGGER.debug("Removing event listener, event: {}, listener {}", eventType, eventListener);
    List<WeakReference<EventListener<?>>> listeners = registry.get(eventType);
    if (null == listeners || listeners.isEmpty()) {
      return false;
    }
    Iterator<WeakReference<EventListener<?>>> iterator = listeners.iterator();
    while (iterator.hasNext()) {
      WeakReference<EventListener<?>> listenerRef = iterator.next();
      EventListener<?> listener = listenerRef.get();
      if (null == listener) {
        iterator.remove();
        continue;
      }
      if (listener.equals(eventListener)) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public <T> void dispatch(T event) {
    long start = System.currentTimeMillis();
    LOGGER.debug("Dispatching: {}", event);
    Class<?> eventType = event.getClass();
    List<WeakReference<EventListener<?>>> listeners = registry.get(eventType);
    if (null == listeners || listeners.isEmpty()) {
      LOGGER.debug("Empty event listeners");
      return;
    }
    LOGGER.debug("listener size: {}", listeners.size());
    Iterator<WeakReference<EventListener<?>>> iterator = listeners.iterator();





    while (iterator.hasNext()) {
      workers.submit(() -> {




        WeakReference<EventListener<?>> listenerRef = iterator.next();
        EventListener<?> listener = listenerRef.get();
        if (null == listener) {
          iterator.remove();
          return;
        }
        LOGGER.debug("Dispatching to {}", listener);
        EventListener<T> eventListener = (EventListener<T>) listener;
        eventListener.onMessage(event);
      });
    }
    long end = System.currentTimeMillis();
    LOGGER.info("used: {}", (end - start));
  }
}
