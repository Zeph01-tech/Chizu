package com.discord.chizu;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import com.discord.utilities.EventWaiter.Waiter;

import java.io.File;
import java.util.Scanner;

import javax.annotation.Nonnull;

public class Chizu extends ListenerAdapter {

  // private static JDA client;
  public static CommandHandler handler;
  public static JDA bot;
  // JDA utilities EventWaiter
  // public static EventWaiter waiter = new EventWaiter();

  // My EventWaiter
  public static Waiter waiter = new Waiter();
	
	public static void main(String[] args) throws Exception {
		File file = new File("./TOKENS/token.txt");
		Scanner content = new Scanner(file);
		String token = content.nextLine();
		content.close();

		bot = JDABuilder
            .createDefault(token)
            .setChunkingFilter(ChunkingFilter.ALL)
            .enableIntents(
              GatewayIntent.GUILD_MESSAGES,
              GatewayIntent.DIRECT_MESSAGES,
              GatewayIntent.DIRECT_MESSAGE_REACTIONS,
              GatewayIntent.GUILD_MEMBERS,
              GatewayIntent.GUILD_MESSAGE_REACTIONS,
              GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
              GatewayIntent.MESSAGE_CONTENT
            ) 
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .addEventListeners(new Chizu())
            .addEventListeners(waiter)
            .setActivity(Activity.watching("L∅Ìf Ìj Ph∅N"))
            .build();
 
     try {
        handler = new CommandHandler();
        handler.setPrefixes(new String[] {"Chizu", "chizu"}).build();

     } catch (Exception e) {
        System.out.println("Error occured");
        System.out.println(e.getStackTrace());
     }
	}

  @Override
  public void onReady(@Nonnull ReadyEvent event){
    handler.adminServer = bot.getGuildById(CommandHandler.adminServerId);
    System.out.println("Ready");
  }
 
	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
    
    handler.execute(event);
	}
}