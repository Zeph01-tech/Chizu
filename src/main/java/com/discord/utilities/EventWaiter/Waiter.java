package com.discord.utilities.EventWaiter;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

  public Waiting_Event(Class<T> type, Predicate<T> check, Consumer<T> callback, int timeout, Runnable timeoutCb) {
    this.type = type;
    this.check = check;
    this.callback = callback;
    this.timeout = timeout;
    this.timeoutCb = timeoutCb;
  }
}

public class Waiter extends ListenerAdapter {
  private List<Waiting_Event<? extends Event>> queue = new ArrayList<>();
  ScheduledExecutorService threadpool = Executors.newScheduledThreadPool(5);

  public <T extends Event> void waitFor(Class<T> _cls, Predicate<T> check, Consumer<T> callback, Runnable timeoutCallback, int timeout) {
    Waiting_Event<T> newEvent = new Waiting_Event<>(_cls, check, callback, timeout, timeoutCallback);
    this.queue.add(newEvent);

    Runnable cancelTaskAfterTimeout = () -> {
      if (this.queue.remove(newEvent))
        newEvent.timeoutCb.run();
    };

    this.threadpool.schedule(cancelTaskAfterTimeout, timeout, TimeUnit.SECONDS);
  }

  @SuppressWarnings("unchecked")
  private <T extends Event> List<Waiting_Event<T>> search(Class<T> eventType) {
    List<Waiting_Event<T>> list = new ArrayList<>();

    this.queue.forEach(item -> {
      if (item.type.equals(eventType)) {
        Waiting_Event<T> _event = (Waiting_Event<T>) item;
        list.add(_event);
      }
    });

    return list;
  }

  private <T extends Event> Waiting_Event<T> checkForEach(List<Waiting_Event<T>> waitingEvents, T firedEvent) {
    for (Waiting_Event<T> event : waitingEvents) {
      if (event.check.test(firedEvent)) {
        this.queue.remove(event);
        return event;
      }
    }
    return null;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    String msg = event.getMessage().getContentRaw();
    if (msg.startsWith("chizu") || msg.startsWith("Chizu")) return;

    List<Waiting_Event<MessageReceivedEvent>> correspondingAvailableEvents = search(MessageReceivedEvent.class);

    if (correspondingAvailableEvents.size() > 0) {
      Waiting_Event<MessageReceivedEvent> toRun = checkForEach(correspondingAvailableEvents, event);
      if (toRun != null)
        toRun.callback.accept(event);
    }
  }

  @Override
  public void onMessageReactionAdd(MessageReactionAddEvent event) {
    List<Waiting_Event<MessageReactionAddEvent>> correspondingAvailableEvents = search(MessageReactionAddEvent.class);

    if (correspondingAvailableEvents.size() > 0) {
      Waiting_Event<MessageReactionAddEvent> toRun = checkForEach(correspondingAvailableEvents, event);
      if (toRun != null)
        toRun.callback.accept(event);
    }
  }

  @Override
  public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
    List<Waiting_Event<MessageReactionRemoveEvent>> correspondingAvailableEvents = search(MessageReactionRemoveEvent.class);

    if (correspondingAvailableEvents.size() > 0) {
      Waiting_Event<MessageReactionRemoveEvent> toRun = checkForEach(correspondingAvailableEvents, event);
      if (toRun != null)
        toRun.callback.accept(event);
    }
  }
}