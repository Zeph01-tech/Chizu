package com.discord.utilities.EventWaiter;

import net.dv8tion.jda.api.events.Event;

public interface CheckCallback {
  public boolean run(Event event);
}