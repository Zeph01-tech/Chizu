package com.discord.chizu.Commands;

import com.discord.chizu.Chizu;
import com.discord.chizu.Command;
import com.discord.chizu.Context;

import net.dv8tion.jda.api.EmbedBuilder;

public class PrefixList extends Command {
  public PrefixList() {
    super("prefixlist", "Fetches a list of all prefixes of bot.", new String[] {"prefixl"}, false);

    this.register();
    System.out.println("PrefixList Registered");
  }
 
  @Override
  public void execute(Context ctx) {
    if (ctx.args.length != 0) return;

    String dialouge = "";
		int ctr = 1;
		for (String prefix : Chizu.handler.prefixes)
			dialouge += ctr++ + ". " + prefix + "\n";

		EmbedBuilder embed = new EmbedBuilder()
                              .setTitle("All Prefixes")
                              .setDescription(dialouge)
                              .setColor(0x3d72b3);

		ctx.channel.sendMessageEmbeds(embed.build()).queue();
  }
}
