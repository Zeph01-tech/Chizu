package com.discord.utilities.EventWaiter;

import java.util.function.Consumer;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.Event;

class Waiting_Event<T extends Event> {
  Class<T> type;
  CheckCallback check;
  Consumer<T> callback;
  int timeout;
  Runnable timeoutCb;

  public Waiting_Event(Class<T> type, CheckCallback check, Consumer<T> callback, int timeout, Runnable timeoutCb) {
    this.type = type;
    this.check = check;
    this.callback = callback;
    this.timeout = timeout;
    this.timeoutCb = timeoutCb;
  }
}

public class Waiter extends ListenerAdapter {
  private List<Waiting_Event<? extends Event>> queue = new ArrayList<>();
  ScheduledExecutorService threadpool = Executors.newScheduledThreadPool(1);

  public <T extends Event> void waitFor(Class<T> _cls, CheckCallback check, Consumer<T> callback, Runnable timeoutCallback, int timeout) {
    Waiting_Event<T> newEvent = new Waiting_Event<>(_cls, check, callback, timeout, timeoutCallback);
    this.queue.add(newEvent);

    Runnable cancelTaskAfterTimeout = () -> {
      if (this.queue.remove(newEvent))
        newEvent.timeoutCb.run();
    };

    this.threadpool.schedule(cancelTaskAfterTimeout, newEvent.timeout, TimeUnit.SECONDS);
  }

  @SuppressWarnings("unchecked")
  <T extends Event> List<Waiting_Event<T>> search(Class<T> eventType) {
    List<Waiting_Event<T>> list = new ArrayList<>();

    for (Waiting_Event<? extends Event> item : this.queue) {
      if (item.type.equals(eventType)) {
        Waiting_Event<T> _event = (Waiting_Event<T>) item;
        list.add(_event);
      }
    }

    return list;
  }

  <T extends Event> Waiting_Event<T> checkForEach(List<Waiting_Event<T>> waitingEvents, T firedEvent) {
    for (Waiting_Event<T> event : waitingEvents) {
      if (event.check.run(firedEvent)) {
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

    List<Waiting_Event<MessageReceivedEvent>> correspondingAvailableEvents = this.search(MessageReceivedEvent.class);

    if (correspondingAvailableEvents.size() > 0) {
      Waiting_Event<MessageReceivedEvent> toRun = this.checkForEach(correspondingAvailableEvents, event);
      if (toRun != null)
        toRun.callback.accept(event);
    }
  }
}