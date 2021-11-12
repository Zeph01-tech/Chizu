package com.discord.chizu;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.security.auth.login.LoginException;

class Context {
	Guild guild;
	User author;
	List<Member> members;
	MessageChannel channel;
	long guild_id, author_id, channel_id;

	public Context(MessageReceivedEvent event) {
		this.guild = event.getGuild();
		this.guild_id = this.guild.getIdLong();
		this.author = event.getAuthor();
		this.members = event.getGuild().getMembers();
		this.author_id = this.author.getIdLong();
		this.channel = event.getChannel();
		this.channel_id = this.channel.getIdLong();
	}

	public Member getMemberById(String id) {
		return this.guild.getMemberById(id);
	}

	public void send(String message) {
		this.channel.sendMessage(message).queue();
	}

	public void send(EmbedBuilder embed) {
		this.channel.sendMessage(embed.build()).queue();
	}
}

class IDs {
	public static long my_id = 762372102204030986L;
}

class funcs {
	public static boolean isValidPrefix(String str) {
		for (String prefix : Chizu.prefixes)
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
	public static String[] prefixes = {"Chizu", "chizu"};
	private static JDA client;
	private static EventWaiter waiter = new EventWaiter();
	
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
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split(" ");

		if (funcs.isValidPrefix(args[0])) {
			if (args[1].equals("help"))
				help(new Context(event));
			else if (args[1].equals("?"))
				answer(new Context(event));
			else if (args[1].equals("addprefix") || args[1].equals("addp")) {
				Context ctx = new Context(event);
				if (args.length == 2)
					ctx.send("Specify a prefix to add");
				else
					addprefix(ctx, args[2]);
				
			} else if (args[1].equals("removeprefix") || args[1].equals("removep")) {
				Context ctx = new Context(event);
				if (args.length == 2) 
					ctx.send("Specify a prefix to remove");
				else
					removeprefix(ctx, args[2]);

			} else if (args[1].equals("prefixlist") || args[1].equals("pl"))
				prefix_list(new Context(event));

			  else if (args[1].equals("greet")) {
				Context ctx = new Context(event);
				if (args.length == 2)
					ctx.send("Mention a user to greet");
				else {
					String id = args[2].replaceAll("<@", " ").replace('!', ' ').replace('>', ' ').strip();
					if (id.equals(args[2]))
						ctx.send("By all means, **I don't see any** `" + args[2] + "` **in our server** " + funcs.random_choice(new String[] {"._.", ".-.", "-_-"}));
					else
						greet(ctx, ctx.getMemberById(id));
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
							kick(ctx, ctx.getMemberById(id), "0");
						else
							kick(ctx, ctx.getMemberById(id), args[3]);
					}
				} else 
					ctx.send("`kick` can only be used by the owner for now");
			} else if (args[1].equals("test"))
				test(new Context(event));
		}
	}

	public void help(Context ctx) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("**Help Panel**");
		embed.setDescription("*List of all commands and their uses.*");
		embed.setColor(0xe06636);
		embed.addField("`?`", "The bot responds with a random response.", false);
		embed.addField("`prefixlist` **Possible aliase(s):** `pl`", "Responds with the list of prefixes the bot listens to.", false);
		embed.addField("`addprefix <new prefix>` **Possible aliase(s):** `addp`", "New prefix is added in the`prefix list`.", false);
		embed.addField("`removeprefix <prefix>` **Possible aliase(s):** `removep`", "Mentioned prefix is removed from the `prefix list`.", false);
		embed.addField("`greet <user>`", "Greets the `user`", false);
		ctx.send(embed);
	}

	public void answer(Context ctx) {
		ctx.send(funcs.random_choice(new String[] {"Yes?", "haii!!", "I'm alive!", "Nan-desuka?", "**wot**", "Yep", "Ur Dog"}));
	}

	public void addprefix(Context ctx, String prefix) {
		if (ctx.author_id == IDs.my_id) {
			String[] new_prefixes = new String[prefixes.length + 1];
			new_prefixes[0] = prefix;
			int ctr = 1;
			for (String each : prefixes)
				new_prefixes[ctr++] = each;
			
			prefixes = new_prefixes;
			ctx.send("Prefix `" + prefix + "` added successfully.");
		}
	}

	public void removeprefix(Context ctx, String prefix) {
		if (ctx.author_id == IDs.my_id) {
			if (!funcs.hasValue(prefixes, prefix)) {
				ctx.send("Prefix `" + prefix + "` not found.");
			} else {
				int ctr = 0;
				String[] new_prefixes = new String[prefixes.length - 1];
				for (String each : prefixes) {
					if (!each.equals(prefix)) {
						new_prefixes[ctr] = each;
						ctr++;
					}
				}
				prefixes = new_prefixes;
				ctx.send("Prefix `" + prefix + "` removed successfully.");
			}
		}
	}

	public void prefix_list(Context ctx) {
		String dialouge = "";
		int ctr = 1;
		for (String prefix : prefixes) {
			dialouge += ctr + ". " + prefix + "\n";
			ctr++;
		}
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("All Prefixes");
		embed.setDescription(dialouge);
		embed.setColor(0x3d72b3);
		ctx.send(embed);
	}

	public void greet(Context ctx, Member member) {
		ctx.send(funcs.random_choice(new String[] {"Hi!! ", "Moshi Moshi!! ", "Hey! "}) + member.getAsMention());
	}

	public void kick(Context ctx, Member member, String reason) {
		try {
			if (reason.equals("0"))
				member.kick().queue();
			else
				member.kick(reason).queue();
			
			ctx.send("Successfully kicked " + member.getUser().getAsTag() + " from the server");
		} catch (Exception e) {
			ctx.send("Couldn't kick " + member.getAsMention() + " for some reason");
		}
	}

	public void test(Context ctx) {
		ctx.send("Send a message " + ctx.author.getAsMention());
		waiter.waitForEvent(GuildMessageReceivedEvent.class, 
		e -> e.getAuthor().getIdLong() == ctx.author_id && e.getChannel().getIdLong() == ctx.channel_id, 
		(e) -> {
			ctx.send("Yay even waiter is working!!");
			waiter.waitForEvent(GuildMessageReceivedEvent.class, (f) -> f.getAuthor().getIdLong() == ctx.author_id && f.getChannel().getIdLong() == ctx.channel_id, 
			(f) -> {
				ctx.send("Bera gaand");
			});
		});
	}
}