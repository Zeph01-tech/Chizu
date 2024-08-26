package com.discord.chizu;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Context {
	public Guild guild;
	public User author;
	public List<Member> members;
	public final Message message;
	public final MessageChannel channel;
	public String guildId;
  public final String authorId, channelId;
  public final String[] args;
  public final MessageReceivedEvent event;

	public Context(MessageReceivedEvent event, String[] args) {
		if (event.isFromGuild()) {
      this.guild = event.getGuild();
  		this.guildId = this.guild.getId();
      this.members = this.guild.getMembers();
    }
		this.author = event.getAuthor();
		this.authorId = this.author.getId();
		this.channel = event.getChannel();
		this.channelId = this.channel.getId();
		this.message = event.getMessage();

    String[] args_ = new String[args.length - 2];

    for (int i = 2; i < args.length; i++)
      args_[i - 2] = args[i];

    this.args = args_;
    this.event = event;
	}
}
