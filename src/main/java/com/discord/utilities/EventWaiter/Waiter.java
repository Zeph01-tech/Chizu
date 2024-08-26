package com.discord.utilities.EventWaiter;

import com.discord.chizu.Chizu;
import com.discord.utilities.HelperFuncs;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.Event;

class Waiting_Event<T extends Event> {
  Class<T> type;
  Predicate<T> check;
  Consumer<T> callback;
  int timeout;
  Runnable timeoutCb;
  String nature;

  Waiting_Event(Class<T> type, Predicate<T> check, Consumer<T> callback, Runnable timeoutCb, int timeout, String eventNature) {
    this.type = type;
    this.check = check;
    this.callback = callback;
    this.timeoutCb = timeoutCb;
    this.timeout = timeout;
    this.nature = eventNature;
  }
}

public class Waiter extends ListenerAdapter {
  private final List<Waiting_Event<? extends Event>> waiterQueue = new ArrayList<>();
  private final List<Waiting_Event<? extends Event>> observerQueue = new ArrayList<>();
  private final ScheduledExecutorService waitingThreadpool = Executors.newScheduledThreadPool(5);
  // private final ExecutorService threadpool = Executors.newCachedThreadPool();

  public <T extends Event> void waitFor(
  Class<T> cls,
  Predicate<T> check,
  Consumer<T> callback,
  Runnable timeoutCallback,
  int timeout
  ) {
    Waiting_Event<T> newEvent = new Waiting_Event<T>(cls, check, callback, timeoutCallback, timeout, "wait");
    this.waiterQueue.add(newEvent);

    Runnable cancelRunnable = () -> {
      synchronized (this.waiterQueue) {
        if (this.waiterQueue.remove(newEvent))
          newEvent.timeoutCb.run();
      }
    };

    this.waitingThreadpool.schedule(cancelRunnable, timeout, TimeUnit.SECONDS);
  }

  public <T extends Event> ScheduledFuture<?> watchFor(Class<T> cls, Predicate<T> check, Consumer<T> callback, int timeout) {
      return watchFor(cls, check, callback, null, timeout);
    }

  public <T extends Event> ScheduledFuture<?> watchFor(
    Class<T> cls,
    Predicate<T> check,
    Consumer<T> callback,
    Runnable timeoutCb,
    int timeout
    ) {
    Waiting_Event<T> newEvent = new Waiting_Event<>(cls, check, callback, timeoutCb, timeout, "watch");
    this.observerQueue.add(newEvent);

    Runnable cancelRunnable = () -> {
      synchronized (this.observerQueue) {
        this.observerQueue.remove(newEvent);
        if (newEvent.timeoutCb != null)
          newEvent.timeoutCb.run();
      }
    };

    return this.waitingThreadpool.schedule(cancelRunnable, timeout, TimeUnit.SECONDS);
  }

  @SuppressWarnings("unchecked")
  private <T extends Event> List<Waiting_Event<T>> search(Class<T> eventType) {
    List<Waiting_Event<T>> list = new LinkedList<>();

    this.waiterQueue.forEach(event -> {
      if (event.type.equals(eventType))
        list.add((Waiting_Event<T>) event);
    });

    this.observerQueue.forEach(event -> {
      if (event.type.equals(eventType))
        list.add((Waiting_Event<T>) event);
    });

    return list;
  }

  private <T extends Event> List<Waiting_Event<T>> filter(List<Waiting_Event<T>> waitingEvents, T firedEvent) {

    List<Waiting_Event<T>> filteredEvents = new LinkedList<>();

    for (Waiting_Event<T> event : waitingEvents)
      if (event.check.test(firedEvent)) {
        if (event.nature.equals("wait"))
          synchronized (this.waiterQueue) {
            this.waiterQueue.remove(event);
          }

        filteredEvents.add(event);
      }

    return filteredEvents;
  }

  private <T extends Event> void handleEvent(List<Waiting_Event<T>> correspondingAvailableEvents, T firedEvent) {
    if (correspondingAvailableEvents.size() == 0) return;
    
    List<Waiting_Event<T>> eventsToRun = filter(correspondingAvailableEvents, firedEvent);
    eventsToRun.forEach(event -> {
      event.callback.accept(firedEvent);
    });
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent firedEvent) {
    if (firedEvent.getAuthor().isBot()) return;
    String msg = firedEvent.getMessage().getContentRaw();

    if (HelperFuncs.hasValue(Chizu.handler.prefixes, msg.split(" ")[0])) return;

    System.out.println("waiter aya");

    handleEvent(search(MessageReceivedEvent.class), firedEvent);
  }

  @Override
  public void onMessageReactionAdd(MessageReactionAddEvent firedEvent) {
    if (firedEvent.getUser().isBot()) return;

    handleEvent(search(MessageReactionAddEvent.class), firedEvent);
  }

  @Override
  public void onMessageReactionRemove(MessageReactionRemoveEvent firedEvent) {
    if (firedEvent.getUser().isBot()) return;

    handleEvent(search(MessageReactionRemoveEvent.class), firedEvent);
  }
}