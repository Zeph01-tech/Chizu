package com.discord.chizu.Commands;

import com.discord.chizu.Chizu;
import com.discord.chizu.Command;
import com.discord.chizu.Context;
import com.discord.utilities.HelperFuncs;

public class RemovePrefix extends Command {
  public RemovePrefix() {
    super("removeprefix", "Removes the specfied prefix from bot.", new String[] {"removep"}, true);

    this.register();
    System.out.println("RemovePrefix Registered");
  }

  @Override 
  public void execute(Context ctx) {
    if (ctx.args.length != 3) return;

    String arguedPrefix = ctx.args[2];
    if (!HelperFuncs.hasValue(Chizu.handler.prefixes, arguedPrefix)) {
      ctx.channel.sendMessage("Prefix `" + arguedPrefix + "` not found.");
      return;
    }

    int ctr = 0;
    String[] newPrefixList = new String[Chizu.handler.prefixes.length - 1];

    for (String prefix : Chizu.handler.prefixes)
      if (!prefix.equals(arguedPrefix))
        newPrefixList[ctr++] = prefix;

    Chizu.handler.prefixes = newPrefixList;
    ctx.channel.sendMessage("Prefix `" + arguedPrefix + "` removed successfully.").queue();
  }
}
