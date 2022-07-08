package com.discord.chizu;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List; 

public class Context {
	public Guild guild;
	public User author;
	public List<Member> members;
	public Message message;
	public MessageChannel channel;
	public long guild_id, author_id, channel_id;
  public String[] args;

	public Context(MessageReceivedEvent event) {
		this.guild = event.getGuild();
		this.guild_id = this.guild.getIdLong();
		this.author = event.getAuthor();
		this.members = this.guild.getMembers();
		this.author_id = this.author.getIdLong();
		this.channel = event.getChannel();
		this.channel_id = this.channel.getIdLong();
		this.message = event.getMessage();
    this.args = event.getMessage().getContentRaw().split(" ");
	}

	public Member getMemberById(String id) {
		return this.guild.getMemberById(id);
	}

	public void send(String message) {
		this.channel.sendMessage(message).queue();
	}

	public void send(EmbedBuilder embed) {
		this.channel.sendMessageEmbeds(embed.build()).queue();
	}

	public void reply(String message) {
		this.message.reply(message).queue();
	}
}
