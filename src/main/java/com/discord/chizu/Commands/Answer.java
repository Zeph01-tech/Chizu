package com.discord.chizu.Commands;

import com.discord.chizu.Command;
import com.discord.chizu.Context;
import com.discord.utilities.HelperFuncs;

public class Answer extends Command {
  public Answer() {
    super("?", "Answers to the User", null, false);

    this.register();
    System.out.println("Answer Registered");
  }
 
  @Override
  public void execute(Context ctx) {
    if (ctx.args.length != 0) return;

    ctx.channel.sendMessage(HelperFuncs.random_choice(new String[] {"Yes?", "haii!!", "I'm alive!", "Nan-desuka?", "**wot**", "Yep", "Ur Dog"})).queue();
  }
}