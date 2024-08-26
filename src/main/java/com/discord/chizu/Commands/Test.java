package com.discord.chizu.Commands;

import com.discord.chizu.Command;
import com.discord.chizu.Context;

import static com.discord.utilities.HelperFuncs.getEmoji;

public class Test extends Command {
  public Test() {
    super("test", "command for temporary testing", null, true);

    this.register();
    System.out.println("Test Registered");
  }

  @Override
  public void execute(Context ctx) {
    ctx.channel.sendMessage(getEmoji("Lmao_no")).queue();
    
  }
}
