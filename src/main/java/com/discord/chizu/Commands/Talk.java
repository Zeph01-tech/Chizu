package com.discord.chizu.Commands;

import com.discord.chizu.*;
import com.discord.utilities.HelperFuncs;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Talk extends Command {
  public Talk() {
    super("talk", "Start a chat with the bot", new String[] {}, true);

    this.register();
    System.out.println("Talk Registered");
  }

  @Override
  public void execute(Context ctx) {

    ArrayList<String> channelIds = alive.get(String.valueOf(ctx.authorId));

    if (channelIds == null) {
      alive.put(String.valueOf(ctx.authorId), new ArrayList<>(Arrays.asList(String.valueOf(ctx.channelId))));
    }

    else if (!HelperFuncs.hasValue(channelIds, String.valueOf(ctx.channelId))) {
      channelIds.add(String.valueOf(ctx.channelId));
    }

    else return;

    System.out.println(alive);

    startChat(ctx);
  }

  private static HashMap<String, ArrayList<String>> responses = new HashMap<>();
  private static HashMap<String, ArrayList<String>> alive = new HashMap<>();

  static {
    responses.put(
    "opening", 
    new ArrayList<>(Arrays.asList(new String[] {"Yoo", "Tell me!", "Wdym \"talk\"? I'm ready for u all the time, tell me"})));

    responses.put("normal",
    new ArrayList<>(Arrays.asList(new String[] {"Oh really?", "wow that's nice, what now", "fuck off."})));

    responses.put("ending-told", 
    new ArrayList<>(Arrays.asList(new String[] {"as u wish, squidward.", "woah okhay, not that you're even worth to have a chat with me huh", "okhay, get lost?", "fine, bye"})));

    responses.put("ending-auto", 
    new ArrayList<>(Arrays.asList(new String[] {"find yourself someone who has more time to spare on you.", "Huh alright I'm busy anyways"})));
  }

  private void chat(MessageReceivedEvent event) {
  
    String res = event.getMessage().getContentRaw();

    if (HelperFuncs.hasValue(new String[] {"shut up", "end chat", "stfu"}, res)) {
      
      endChat(event, true);
    } else {

      String authorId = event.getAuthor().getId();
      ArrayList<String> channels = alive.get(authorId);

      if (channels == null) return;

      else if (!HelperFuncs.hasValue(channels, event.getChannel().getId())) return;

      ArrayList<String> availableRes = responses.get("normal");

      event.getChannel().sendMessage(HelperFuncs.random_choice(availableRes)).queue();

      Chizu.waiter.waitFor(MessageReceivedEvent.class, (e) -> e.getAuthor().getIdLong() == event.getAuthor().getIdLong() && e.getChannel().getId().equals(event.getChannel().getId()), (e) -> chat(e), () -> endChat(event, false), 10);
    }
  }

  private void startChat(Context ctx) {

    ctx.channel.sendMessage(HelperFuncs.random_choice(responses.get("opening"))).queue();

    Chizu.waiter.waitFor(MessageReceivedEvent.class, e -> e.getMember().getIdLong() == ctx.authorId && e.getChannel().getId().equals(String.valueOf(ctx.channelId)), e -> chat(e), () -> endChat(ctx.event, false), 10);
  }

  private void endChat(MessageReceivedEvent event, boolean told) {
    String authorId = event.getAuthor().getId();
    ArrayList<String> channels = alive.get(authorId);
    channels.remove(event.getChannel().getId());
    alive.put(authorId, channels);

    if (alive.get(authorId).size() == 0) alive.remove(authorId);

    System.out.println(alive);
    if (told)
      event.getChannel().sendMessage(HelperFuncs.random_choice(responses.get("ending-told"))).queue();
    else 
      event.getChannel().sendMessage(HelperFuncs.random_choice(responses.get("ending-auto"))).queue();
  }
}