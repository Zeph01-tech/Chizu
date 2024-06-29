package com.discord.chizu.Commands;

import com.discord.chizu.Command;
import com.discord.chizu.Context;

import java.util.Arrays;
import java.util.ArrayList;

import com.discord.chizu.Chizu;

public class AddPrefix extends Command {
  public AddPrefix() {
    super("addprefix", "Adds a new prefix to bot.", new String[] {"addp"}, true);

    this.register();
    System.out.println("AddPrefix Registered");
  }

  @Override 
  public void execute(Context ctx) {
    if (ctx.args.length != 1) return;

    ArrayList<String> prefixL = new ArrayList<>(Arrays.asList(Chizu.handler.prefixes));
    prefixL.add(ctx.args[0]);

    Chizu.handler.prefixes = prefixL.toArray(new String[] {});

    ctx.message.reply("Prefixes updated ✅").queue();
  }
}
