package com.discord.chizu.Commands;

import java.text.MessageFormat;

import com.discord.chizu.Chizu;
import com.discord.chizu.Command;
import com.discord.chizu.Context;

import net.dv8tion.jda.api.entities.Member;


public class CustomAdminNotif extends Command {
  
  public CustomAdminNotif() {
    super("notify", "Secret ( ͡° ͜ʖ ͡°)", new String[] {}, true);
    
    this.register();
    System.out.println("Notify registered");
  }
// chizu notify <all/<member-id>> <msg>
  @Override
  public void execute(Context ctx) {
    if (ctx.args.length == 3) return;

    String notification = "";
    for (int i = 3; i < ctx.args.length; i++) {
      if (i != ctx.args.length - 1)
        notification += ctx.args[i] + " ";
      else
        notification += ctx.args[i];
    }

    try {
      if (ctx.args[2].equals("all"))
        notification += " @everyone" ;
  
      else {
        Member user = Chizu.handler.adminServer.getMemberById(ctx.args[2]);
        if (user != null)
          notification += " " + user.getAsMention();
        
        else {
          ctx.message.reply(MessageFormat.format("User with id {0} not found", ctx.args[2])).queue();
          return;
        }
      }
  
      Chizu.handler.adminServer.getTextChannelsByName("☞☞༼𝙶𝚎𝚗𝚎𝚛𝚊𝚕༽☯✿", true)
      .get(0)
      .sendMessage(notification).queue();
    } catch (Exception e) {
      ctx.message.reply(e.getMessage()).queue();
    }
  }
}
