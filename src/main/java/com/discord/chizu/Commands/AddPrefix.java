package com.discord.chizu.Commands;

import com.discord.chizu.Command;
import com.discord.chizu.Context;

import net.dv8tion.jda.api.entities.Emote;

import com.discord.chizu.Chizu;

public class AddPrefix extends Command {
  public AddPrefix() {
    super("addprefix", "Adds a new prefix to bot.", new String[] {"addp"}, true);

    this.register();
    System.out.println("AddPrefix Registered");
  }

  @Override 
  public void execute(Context ctx) {
    if (ctx.args.length != 3) return;

    String[] newPrefixList = new String[Chizu.handler.prefixes.length + 1];
    int ctr = 1;

    for (String prefix : Chizu.handler.prefixes)
    newPrefixList[ctr++] = prefix;

    Chizu.handler.prefixes = newPrefixList;


    ctx.message.reply("Prefixes updated ✅").queue();
  }
}
