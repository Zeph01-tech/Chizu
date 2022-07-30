package com.discord.chizu;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Context {
	public Guild guild;
	public User author;
	public List<Member> members;
	public Message message;
	public MessageChannel channel;
	public long guildId, authorId, channelId;
  public String[] args;

	public Context(MessageReceivedEvent event, String[] args) {
		this.guild = event.getGuild();
		this.guildId = this.guild.getIdLong();
		this.author = event.getAuthor();
		this.members = this.guild.getMembers();
		this.authorId = this.author.getIdLong();
		this.channel = event.getChannel();
		this.channelId = this.channel.getIdLong();
		this.message = event.getMessage();
    this.args = args;
	}

	public Member getMemberById(String id) {
		return this.guild.getMemberById(id);
	}

	// public void send(String message) {
	// 	this.channel.sendMessage(message).queue();
	// }

  // public void send(String message, Consumer<Message> callback) {
  //   this.channel.sendMessage(message).submit().thenAccept(callback);
  // }


  // public Message send(String message, String _null) {     // fake param {_null} to overload returnable function successfully
  //   return this.channel.sendMessage(message).complete();
  // }

	// public void send(EmbedBuilder embed) {
	// 	this.channel.sendMessageEmbeds(embed.build()).queue();
	// }

  // public Message send(EmbedBuilder embed, String _null) { // fake param {_null} to overload returnable function successfully
  //   return this.channel.sendMessageEmbeds(embed.build()).complete();
  // }

	// public void reply(String message) {
	// 	this.message.reply(message).queue();
	// }
}
