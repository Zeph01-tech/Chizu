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
		if (event.isFromGuild()) {
      this.guild = event.getGuild();
  		this.guildId = this.guild.getIdLong();
      this.members = this.guild.getMembers();
    }
		this.author = event.getAuthor();
		this.authorId = this.author.getIdLong();
		this.channel = event.getChannel();
		this.channelId = this.channel.getIdLong();
		this.message = event.getMessage();
    this.args = args;
	}
}
