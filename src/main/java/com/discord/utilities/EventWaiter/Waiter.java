package com.discord.utilities.EventWaiter;

import java.util.function.Consumer;
import java.util.function.Predicate;

// import javax.annotation.Nonnull;

import com.discord.chizu.Chizu;
import com.discord.utilities.HelperFuncs;

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
  private ScheduledExecutorService threadpool = Executors.newScheduledThreadPool(5);

  public <T extends Event> void waitFor(Class<T> _cls, Predicate<T> check, Consumer<T> callback, Runnable timeoutCallback, int timeout) {
    Waiting_Event<T> newEvent = new Waiting_Event<T>(_cls, check, callback, timeout, timeoutCallback);
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

  private <T extends Event> void handleEvent(List<Waiting_Event<T>> correspondingAvailableEvents, T firedEvent) {
    if (correspondingAvailableEvents.size() > 0) {
      Waiting_Event<T> toRun = checkForEach(correspondingAvailableEvents, firedEvent);
      if (toRun != null)
        toRun.callback.accept(firedEvent); 
    }
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent firedEvent) {
    if (firedEvent.getAuthor().isBot()) return;
    String msg = firedEvent.getMessage().getContentRaw();

    if (HelperFuncs.hasValue(Chizu.handler.prefixes, msg.split(" ")[0])) return;

    handleEvent(search(MessageReceivedEvent.class), firedEvent);
  }

  @Override
  public void onMessageReactionAdd(MessageReactionAddEvent firedEvent) {

    handleEvent(search(MessageReactionAddEvent.class), firedEvent);
  }

  @Override
  public void onMessageReactionRemove(MessageReactionRemoveEvent firedEvent) {
    
    handleEvent(search(MessageReactionRemoveEvent.class), firedEvent);
  }
}