package com.discord.chizu.Commands;

import java.text.MessageFormat;

import com.discord.chizu.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class Help extends Command {
  public Help() {
    super("Help", "Help Panel", new String[] {"help"}, false);

    this.register();
    System.out.println("Help Registered");
  }

  @Override
  public void execute(Context ctx) {
    if (ctx.args.length != 2) return;
 
    EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("**Help Panel**")
    .setDescription("*List of all commands and their uses.*")
    .setColor(0xe06636);

    for (Command cmd : Chizu.handler.commands)
      if (!cmd.name.equalsIgnoreCase("help"))
        embed.addField(MessageFormat.format("`{0}`", cmd.name), cmd.description, false);

		ctx.channel.sendMessageEmbeds(embed.build()).queue();
  }
}