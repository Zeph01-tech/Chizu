package com.discord.utilities.EventWaiter;

import net.dv8tion.jda.api.events.Event;

public interface CheckCallback<T extends Event> {
  public boolean run(T event);
}