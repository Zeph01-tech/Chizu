package com.discord.chizu.Commands;

import java.text.MessageFormat;
import java.util.List;

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
    if (ctx.args.length != 0) return;

    EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("**Help Panel**")
    .setDescription("*List of all commands and their uses.*")
    .setColor(0xe06636);

    Chizu.handler.commandsThroughNames.values().forEach(cmd -> {
      if (!cmd.name.equalsIgnoreCase("help"))
        embed.addField(MessageFormat.format("`{0}`{1}", cmd.name, sortAliases(cmd.aliases)), cmd.description, false);
    });

		ctx.channel.sendMessageEmbeds(embed.build()).queue();
  }

  private String sortAliases(List<String> aliases) {
    if (aliases.size() == 0) return "";

    StringBuilder query = new StringBuilder(" - aka *`");
    aliases.forEach(alias -> {
      query.append(alias + "/");
    });
    
    return query.deleteCharAt(query.length() - 1).append("`*").toString();
  }
}