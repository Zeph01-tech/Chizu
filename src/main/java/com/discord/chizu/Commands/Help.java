package com.discord.chizu.Commands;

import com.discord.chizu.Command;
import com.discord.chizu.Context;
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
		embed.setTitle("**Help Panel**");
		embed.setDescription("*List of all commands and their uses.*");
		embed.setColor(0xe06636);
		embed.addField("`?`", "The bot responds with a random response.", false);
		embed.addField("`prefixlist` **Possible aliase(s):** `pl`", "Responds with the list of prefixes the bot listens to.", false);
		embed.addField("`addprefix <new prefix>` **Possible aliase(s):** `addp`", "New prefix is added in the`prefix list`.", false);
		embed.addField("`removeprefix <prefix>` **Possible aliase(s):** `removep`", "Mentioned prefix is removed from the `prefix list`.", false);
		embed.addField("`greet <user>`", "Greets the `user`", false);
		embed.addField("`kick <user>`", "Kicks the mentioned `user` if the member who used the command has the kick perms", false);
		ctx.channel.sendMessageEmbeds(embed.build()).queue();
  }
}