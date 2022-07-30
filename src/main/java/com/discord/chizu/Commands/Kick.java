package com.discord.chizu.Commands;

import com.discord.chizu.Chizu;
import com.discord.chizu.Command;
import com.discord.chizu.Context;
import com.discord.utilities.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Kick extends Command {
  public Kick() {
    super("kick", "Kick a dick straight outside.", new String[] {}, true);

    this.register();
    System.out.println("Kick Registered");
  }
 
  @Override
  public void execute(Context ctx) {
    if (ctx.args.length != 3) return;

    Member target = ctx.getMemberById (
      ctx.args[2].replaceAll("<@", " ").replace("!", " ").replace(">", " ").strip()
    );

    if (target == null) {
      ctx.channel.sendMessage("Could not find the provided member.");
      return;
    }

    ctx.channel.sendMessage("Are you sure you want to kick " + target.getAsMention() + " from the server?\nReply with either yes/y or no/n.");
			Chizu.waiter.waitForEvent(GuildMessageReceivedEvent.class, 
			e -> e.getChannel().getIdLong() == ctx.channelId && e.getAuthor().getIdLong() == ctx.authorId && Utils.hasValueIgnoreCase(new String[] {"Yes", "Y", "No", "N"}, e.getMessage().getContentRaw()), 
			e -> {
				String msg = e.getMessage().getContentRaw();
				if (msg.equalsIgnoreCase("No") || msg.equalsIgnoreCase("N")) {
					EmbedBuilder embed = new EmbedBuilder()
                                    .setDescription("Command Cancelled.")
                                    .setColor(0xcf0418);
					ctx.channel.sendMessageEmbeds(embed.build());
				} else {
					String tag = target.getUser().getAsTag();
					EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle("Successfully kicked.")
                                    .setDescription(tag + " has been kicked from the server.");
                                    
          target.kick().queue();
          ctx.channel.sendMessageEmbeds(embed.build()).queue();
					// if (reason.equals("0"))
					// 	target.kick().queue();
					// else
					// 	target.kick(reason).queue();
					// ctx.send(embed);
				}
			});
  }
}
