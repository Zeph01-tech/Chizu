package com.discord.chizu;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import javax.security.auth.login.LoginException;

import com.discord.chizu.Context;
import com.discord.chizu.Commands;

class IDs {
	public static long my_id = 762372102204030986L;
}

class funcs {
	public static boolean isValidPrefix(String str) {
		for (String prefix : Commands.prefixes)
			if (prefix.equals(str))
				return true;

		return false;
	}

	public static boolean hasValue(String[] arr, String value) {
		for (String str : arr)
			if (str.equals(value))
				return true;

		return false;
	}

	public static boolean hasValueIgnoreCase(String[] arr, String value) {
		for (String str : arr)
			if (str.equalsIgnoreCase(value))
				return true;

		return false;
	}

	public static String mergearr(String[] arr) {
		String out = "";
		for (String str : arr)
			out += str + " ";

		return out;
	}

	public static String random_choice(String[] arr) {
		return arr[new Random().nextInt(arr.length-1)];
	}

	public static int random_choice(int[] arr) {
		return arr[new Random().nextInt(arr.length-1)];
	}
}

public class Chizu extends ListenerAdapter {
	
	public static void main(String[] args) throws LoginException, FileNotFoundException {
		File file = new File("./TOKENS/token.txt");
		Scanner content = new Scanner(file);
		String token = content.nextLine();
		content.close();

		JDABuilder
					.createDefault(token)
					.setChunkingFilter(ChunkingFilter.ALL)
					.enableIntents(
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_MEMBERS,
						GatewayIntent.GUILD_MESSAGE_REACTIONS,
						GatewayIntent.GUILD_EMOJIS
					)
					.addEventListeners(new Chizu())
					.addEventListeners(Commands.waiter)
					.setActivity(Activity.watching("L∅Ìf Ìj Ph∅N"))
					.build();
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split(" ");

		if (funcs.isValidPrefix(args[0])) {
			if (args[1].equals("help") && args.length == 2)
				Commands.help(new Context(event));
			else if (args[1].equals("?") && args.length == 2)
				Commands.answer(new Context(event));
			else if (args[1].equals("addprefix") || args[1].equals("addp")) {
				Context ctx = new Context(event);
				if (args.length == 2)
					ctx.send("Specify a prefix to add");
				else
					Commands.addprefix(ctx, args[2]);
				
			} else if (args[1].equals("removeprefix") || args[1].equals("removep")) {
				Context ctx = new Context(event);
				if (args.length == 2) 
					ctx.send("Specify a prefix to remove");
				else
					Commands.removeprefix(ctx, args[2]);

			} else if (args[1].equals("prefixlist") || args[1].equals("pl"))
				Commands.prefix_list(new Context(event));

			  else if (args[1].equals("greet")) {
				Context ctx = new Context(event);
				if (args.length == 2)
					ctx.send("Mention a user to greet");
				else {
					String id = args[2].replaceAll("<@", " ").replace('!', ' ').replace('>', ' ').strip();
					if (id.equals(args[2]))
						ctx.send("By all means, **I don't see any** `" + args[2] + "` **in our server** " + funcs.random_choice(new String[] {"._.", ".-.", "-_-"}));
					else
						Commands.greet(ctx, ctx.getMemberById(id));
				}
			} else if (args[1].equals("kick")) {
				Context ctx = new Context(event);
				if (ctx.author_id == IDs.my_id) {
					if (args.length == 2)
						ctx.send("Mention a user to kick");
					else {
						String id = args[2].replaceAll("<@", " ").replace("!", " ").replace(">", " ").strip();
						if (id.equals(args[2]))
							ctx.send("By all means, **I don't see any** `" + args[2] + "` **in our server** " + funcs.random_choice(new String[] {"._.", ".-.", "-_-"}));
						else if (args.length == 3)
							Commands.kick(ctx, ctx.getMemberById(id), "0");
						else
							Commands.kick(ctx, ctx.getMemberById(id), args[3]);
					}
				} else 
					ctx.send("`kick` can only be used by the owner for now");
			} else if (args[1].equals("test")) {
				Commands.test(new Context(event));
			}
		}
	}
}