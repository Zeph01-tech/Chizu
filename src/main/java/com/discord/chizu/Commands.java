package com.discord.chizu;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

class Test {
	public static String name;
	public static String Mom_name;
	public static String pp_length;

	public static void clear_vals() {
		name = "";
		Mom_name = "";
		pp_length = "";
	}
}

public class Commands {
    public static String[] prefixes = {"Chizu", "chizu"};
    public static EventWaiter waiter = new EventWaiter();

    public static void help(Context ctx) {
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
		ctx.send(embed);
	}

	public static void answer(Context ctx) {
		ctx.send(funcs.random_choice(new String[] {"Yes?", "haii!!", "I'm alive!", "Nan-desuka?", "**wot**", "Yep", "Ur Dog"}));
	}

	public static void addprefix(Context ctx, String prefix) {
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

	public static void removeprefix(Context ctx, String prefix) {
		if (ctx.author_id == IDs.my_id) {
			if (!funcs.hasValue(prefixes, prefix))
				ctx.send("Prefix `" + prefix + "` not found.");
			else {
				int ctr = 0;
				String[] new_prefixes = new String[prefixes.length - 1];
				for (String each : prefixes)
					if (!each.equals(prefix))
						new_prefixes[ctr++] = each;

				prefixes = new_prefixes;
				ctx.send("Prefix `" + prefix + "` removed successfully.");
			}
		}
	}

	public static void prefix_list(Context ctx) {
		String dialouge = "";
		int ctr = 1;
		for (String prefix : prefixes)
			dialouge += ctr++ + ". " + prefix + "\n";

		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("All Prefixes");
		embed.setDescription(dialouge);
		embed.setColor(0x3d72b3);
		ctx.send(embed);
	}

	public static void greet(Context ctx, Member member) {
		ctx.send(funcs.random_choice(new String[] {"Hi!! ", "Moshi Moshi!! ", "Hey! "}) + member.getAsMention());
	}

	public static void kick(Context ctx, Member member, String reason) {
		try {
			ctx.send("Are you sure you want to kick " + member.getAsMention() + " from the server?\nReply with either yes/y or no/n.");
			waiter.waitForEvent(GuildMessageReceivedEvent.class, 
			e -> e.getChannel().getIdLong() == ctx.channel_id && e.getAuthor().getIdLong() == ctx.author_id && funcs.hasValueIgnoreCase(new String[] {"Yes", "Y", "No", "N"}, e.getMessage().getContentRaw()), 
			e -> {
				String msg = e.getMessage().getContentRaw();
				if (msg.equalsIgnoreCase("No") || msg.equalsIgnoreCase("N")) {
					EmbedBuilder embed = new EmbedBuilder();
					embed.setDescription("Command Cancelled.");
					embed.setColor(0xcf0418);
					ctx.send(embed);
				} else {
					String tag = member.getUser().getAsTag();
					EmbedBuilder embed = new EmbedBuilder();
					embed.setTitle("Successfully kicked.");
					embed.setDescription(tag + " has been kicked from the server.");
					if (reason.equals("0"))
						member.kick().queue();
					else
						member.kick(reason).queue();
					ctx.send(embed);
				}
			});
		} catch (Exception e) {
			System.out.println(e);
			ctx.send("Couldn't kick " + member.getAsMention() + " for some reason");
		}
	}

	public static void test(Context ctx) {
		ctx.reply("What's your name?");
		waiter.waitForEvent(GuildMessageReceivedEvent.class,
		e -> e.getAuthor().getIdLong() == ctx.author_id && e.getChannel().getIdLong() == ctx.channel_id,
		e -> {
			Test.name = e.getMessage().getContentRaw();
			e.getMessage().reply("Okhay so, what is your mom's name?").queue();
			waiter.waitForEvent(GuildMessageReceivedEvent.class, f -> f.getAuthor().getIdLong() == ctx.author_id && f.getChannel().getIdLong() == ctx.channel_id, 
			f -> {
				Test.Mom_name = f.getMessage().getContentRaw();
				f.getMessage().reply("Okhay and lastly......your pp size?").queue();
				waiter.waitForEvent(GuildMessageReceivedEvent.class, g -> g.getAuthor().getIdLong() == ctx.author_id && g.getChannel().getIdLong() == ctx.channel_id, 
				g -> {
					Test.pp_length = g.getMessage().getContentRaw();
					ctx.send("So " + Test.name + " who's mom is named " + Test.Mom_name + " and whose pp is lengthed " + funcs.random_choice(new String[] {"1mm", "2mm", Test.pp_length, "0mm"}) + ".....how are you huh?");
				});});});

		Test.clear_vals();
	}
}