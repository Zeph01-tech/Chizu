package com.discord.chizu.Commands;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


import java.util.ArrayList;

import com.discord.chizu.Chizu;
import com.discord.chizu.Command;
import com.discord.chizu.Context;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

class Player {

  private static final int DEFAULT_LIFES = 3;
  final String name, mention;
  int lifes, points;

  Player(Member member, Integer lifes) {
    this.name = member.getEffectiveName();
    this.mention = member.getAsMention();
    this.lifes = lifes == null ? DEFAULT_LIFES : lifes;
    this.points = 0;
  }
}

class Game {

  private final List<String> usedPlaces;
  private final List<Player> players;

  public Game(List<Player> players) {
    usedPlaces = new ArrayList<>();
    this.players = players;
  }
}

public class Atlas extends Command {

  public Atlas() {
    super("atlas", "Initiates an Atlas game, requires atleast two people to start the game.", null, false);

    this.register();
    System.out.println("Atlas registered.");
  }

  @Override
  public void execute(Context ctx) {
    EmbedBuilder embed = new EmbedBuilder()
      .setTitle("**Atlas**")
      .setDescription("You have 15 seconds to react on this message with the check emoji to participate ✅")
      .setFooter("Good Luck! 🟢", "https://cdn.discordapp.com/emojis/892751786719469598.gif?size=56");

    List<Member> players = new ArrayList<>();

    AtomicReference<Message> messageRef = new AtomicReference<>();
    ctx.channel.sendMessageEmbeds(embed.build()).queue(message -> {
      message.addReaction(Emoji.fromUnicode("✅")).queue();
      messageRef.set(message);
    });

    Chizu.waiter.watchFor(MessageReactionAddEvent.class, e -> e.getEmoji().getName().equals("✅"),
    event -> {
      players.add(event.getMember());
      System.out.println(event.getUser().getName() + " reacted with " + event.getEmoji().getName());
      System.out.println("\n" + "Players: " + players);
    }, 10);


    Chizu.waiter.watchFor(MessageReactionRemoveEvent.class, e  -> e.getEmoji().getName().equals("✅"),
    event -> {
      players.remove(event.getMember());
      System.out.println(event.getUser().getName() + " removed their reaction " + event.getEmoji().getName());
      System.out.println("\n" + "Players: " + players);
    }, 10);


    // Game game = new Game();
    
  }
}
