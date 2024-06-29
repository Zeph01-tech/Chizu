package com.discord.chizu.Commands;

import com.discord.chizu.Chizu;
import com.discord.chizu.Command;
import com.discord.chizu.Context;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Kick extends Command {
  public Kick() {
    super("kick", "Kick a dick straight outside.", new String[] {}, true);

    this.register();
    System.out.println("Kick Registered");
  }
 
  @Override
  public void execute(Context ctx) {
    if (ctx.args.length != 1) return;

    Member target = ctx.guild.getMemberById (
      ctx.args[0].replaceAll("<@", " ").replace("!", " ").replace(">", " ").strip()
    );

    if (target == null) {
      ctx.channel.sendMessage("Could not find the provided member.").queue();
      return;
    }

    ctx.channel.sendMessage("Are you sure you want to kick " + target.getAsMention() + " from the server?\nReply with either yes/y or no/n.").queue();
    Chizu.waiter.waitFor(MessageReceivedEvent.class, 
    e -> e.getAuthor().equals(ctx.author) && e.getChannel().equals(ctx.channel), 
    e -> {
      String response = e.getMessage().getContentRaw();
      if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
        EmbedBuilder embed = new EmbedBuilder()
                                .setDescription("Command Cancelled.")
                                .setColor(0xcf0418);

        ctx.channel.sendMessageEmbeds(embed.build()).queue();
      } else if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
          String globalName = target.getUser().getGlobalName();
					EmbedBuilder embed = new EmbedBuilder()
                                  .setTitle("Successfully kicked.")
                                  .setDescription(globalName + " has been kicked from the server.");
                                    
          target.kick().queue();
          ctx.channel.sendMessageEmbeds(embed.build()).queue();
      }
    }, 
    () -> {
      EmbedBuilder embed = new EmbedBuilder()
                              .setDescription("You did not respond in time.\nCommand Cancelled.")
                              .setColor(0xcf0418);

      ctx.channel.sendMessageEmbeds(embed.build()).queue();
    }, 10);
  }
}
