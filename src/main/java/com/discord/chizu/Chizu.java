package com.discord.chizu;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.security.auth.login.LoginException;

public class Chizu extends ListenerAdapter {

  private static JDA client;
  public static CommandHandler handler;
  public static EventWaiter waiter = new EventWaiter();
	
	public static void main(String[] args) throws LoginException, FileNotFoundException {
		File file = new File("./TOKENS/token.txt");
		Scanner content = new Scanner(file);
		String token = content.nextLine();
		content.close();

		client = JDABuilder
                  .createDefault(token)
                  .setChunkingFilter(ChunkingFilter.ALL)
                  .enableIntents(
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_EMOJIS
                  )
                  .addEventListeners(new Chizu())
                  .addEventListeners(waiter)
                  .setActivity(Activity.watching("L∅Ìf Ìj Ph∅N"))
                  .build();

    try {
      handler = new CommandHandler(new String[] {"Chizu", "chizu"});
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getJDA().equals(client)) return;

    handler.execute(event);
	}
}