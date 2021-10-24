package com.discord.chizu;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import org.jetbrains.annotations.NotNull;
import java.util.Random;
import javax.security.auth.login.LoginException;

class Context {
	Guild guild;
	User author;
	MessageChannel channel;
	long guildid, authorid, channelid;

	public Context(MessageReceivedEvent event) {
		this.guild = event.getGuild();
		this.guildid = this.guild.getIdLong();
		this.author = event.getAuthor();
		this.authorid = this.author.getIdLong();
		this.channel = event.getChannel();
		this.channelid = this.channel.getIdLong();
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
		for (String prefix : Chizu.prefixes) {
			if (prefix.equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasValue(String[] arr, String value) {
		for (String str : arr) {
			if (str.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static String mergearr(String[] arr) {
		String out = "";
		for (String str : arr) {
			out += str + " ";
		}
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

	public static void main(String[] args) throws LoginException {
		client = JDABuilder
					.createDefault("ODk2ODI2OTQwMzI4MTI4NTIz.YWMxLA.k6Zm4wsGXhJp6G0Oo9uDM0ewhfE")
					.setChunkingFilter(ChunkingFilter.ALL)
					.enableIntents(
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_MEMBERS
					)
					.addEventListeners(new Chizu())
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
				if (args.length == 2) {
					if (event.getAuthor().getIdLong() == IDs.my_id)
						event.getChannel().sendMessage("Specify a prefix to add").queue();
				}
				else
					addprefix(new Context(event), args[2]);
			} else if (args[1].equals("removeprefix") || args[1].equals("removep")) {
				removeprefix(new Context(event), args[2]);
			} else if (args[1].equals("prefixlist") || args[1].equals("pl")) {
				prefix_list(new Context(event));
			} else if (args[1].equals("greet")) {
				System.out.println(funcs.mergearr(args));
				if (args.length == 2) {
					event.getChannel().sendMessage("Mention a user to greet").queue();
				} else {
					String id = args[2].replaceAll("<@", " ").replace('!', ' ').replace('>', ' ').strip();
					if (id.equals(args[2])) {
						event.getChannel().sendMessage("By all means, **I don't see any** `" + args[2] + "` **in our server** .-.").queue();
					} else {
						Member member = event.getGuild().getMemberById(id);
						greet(new Context(event), member);
					}
				}
			} else if (args[1].equals("test")) {
					System.out.println(args[2]);
			}
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
		String[] choices = {"Yes?", "haii!!", "I'm alive!", "Nan-desuka?"};
		ctx.send(funcs.random_choice(choices));
	}

	public void addprefix(Context ctx, String prefix) {
		if (ctx.authorid == IDs.my_id) {
			String[] new_prefixes = new String[prefixes.length + 1];
			new_prefixes[0] = prefix;
			int ctr = 0;
			for (int i = 1; i < new_prefixes.length; i++) {
				new_prefixes[i] = prefixes[ctr];
				ctr++;
			}
			prefixes = new_prefixes;
			ctx.send("Prefix `" + prefix + "` added successfully.");
		}
	}

	public void removeprefix(Context ctx, String prefix) {
		if (ctx.authorid == IDs.my_id) {
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
		String[] choices = {"Hi!! ", "Moshi Moshi!! ", "Hey! "};
		ctx.send(funcs.random_choice(choices) + member.getAsMention());
	}
}