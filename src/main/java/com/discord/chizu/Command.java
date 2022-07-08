package com.discord.chizu;

public class Command {
  String name, description, aliases[];
  boolean adminOnly;

  public Command(String name, String description, String[] aliases, boolean adminOnly) {
    this.name = name;
    this.description = description;
    this.aliases = aliases;
    this.adminOnly = adminOnly;
  }

  public void register() {
    CommandHandler.register(this);
  }

  public boolean isAlias(String name) {
    for (String alias : this.aliases)
      if (name.equals(alias))
        return true;

    return false;
  }

  public void execute(Context ctx) {
  }
}