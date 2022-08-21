package com.discord.chizu.Commands;

import com.discord.chizu.*;
import com.discord.utilities.HelperFuncs;

import net.dv8tion.jda.api.EmbedBuilder;

public class HighFive extends Command {

  private String[] gif_urls = {
    "https://c.tenor.com/rU9PEKyn4BsAAAAS/fairy-tail-nalu.gif",
    "https://c.tenor.com/1SKzPjLVtrIAAAAS/friends-high5.gif",
    "https://c.tenor.com/XCxQsu3a-7kAAAAS/totodile-pokemon.gif"
  };

  public HighFive() {
    super("highfive", "Highfivessss the user :P", new String[] {}, false);

    this.register();
    System.out.println("HighFive Registered");
  }

  @Override
  public void execute(Context ctx) {
    if (ctx.args.length != 2) return;

    EmbedBuilder embed = new EmbedBuilder();
    embed.setImage(HelperFuncs.random_choice(gif_urls));

    ctx.message.replyEmbeds(embed.build()).queue();
  }
}
