package com.discord.chizu;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
  public String name, description;
  public List<String> aliases;
  public boolean adminOnly;

  public Command(String name, String description, String[] aliases, boolean adminOnly) {
    this.name = name;
    this.description = description;
    this.aliases = Arrays.asList(aliases);
    this.adminOnly = adminOnly;
  }

  public void register() {
    Chizu.handler.register(this);
  }

  public boolean isAlias(String name) { 
    for (String alias : this.aliases)
      if (name.equals(alias))
        return true;

    return false;
  }

  public abstract void execute(Context ctx);
}