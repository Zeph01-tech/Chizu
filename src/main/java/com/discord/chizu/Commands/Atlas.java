package com.discord.chizu.Commands;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import java.util.List;
import java.util.LinkedList;

import com.discord.chizu.Command;
import com.discord.chizu.Context;
import com.discord.chizu.models.AtlasModel;
import static com.discord.chizu.Chizu.waiter;
import static com.discord.utilities.HelperFuncs.fromSameAuthor;
import static com.discord.utilities.HelperFuncs.fromSameServer;
import static com.discord.utilities.HelperFuncs.random_choice;
import static com.discord.utilities.HelperFuncs.randint;

import java.sql.SQLException;
import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import com.mysqlorm.module.Database;

class Player {
  final String name, mention;
  int lifes, points;

  Player(Member member, Integer lifes) {
    this.name = member.getEffectiveName();
    this.mention = member.getAsMention();
    this.lifes = lifes;
    this.points = 0;
  }
}

class Game {
  private final Database db;
  private final List<String> usedPlaces;
  private final List<Player> players;
  private final  MessageChannel channel;
  private Player currentPlayer;
  
  public Game(List<Player> players, MessageChannel channel) {
    this.usedPlaces = new ArrayList<>();
    this.players = players;
    this.channel = channel;
    this.currentPlayer = random_choice(this.players);
    this.db = new Database("localhost", "root", "chizu", "mysql");
  }

  private void ask(boolean started) {
    if (started) {
      this.channel.sendMessage(this.currentPlayer.mention + " Your turn, name country or any national capital that starts with `" + (char) (randint(26) + 65) + '`');
      
      return;
    }


  }

  private void ask() {
    ask(false);
  }

  public void start() {
    try {
      // Connecting to database.
      db.connect();
      
      this.ask(true);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

public class Atlas extends Command {

  private static final int DEFULT_PLAYER_LIFES = 3;

  public Atlas() {
    super("atlas", "Initiates an Atlas game, requires atleast two people to start the game :/", null, false);

    this.register();
    System.out.println("Atlas registered.");
  }

  private static String getPlayability(int playerCount) {
    if (playerCount == 2) return "1v1 huh";
    if (playerCount > 2 && playerCount < 4) return "Enjoyable";
    if (playerCount >= 4) return "Perfect";

    return "Just don't rage quit y'all XD";
  }

  private static boolean validLifeSetResponse(String content) {
    try {
      Integer.parseInt(content);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  public void execute(Context ctx) {
    EmbedBuilder embed = new EmbedBuilder()
      .setTitle("**Atlas**")
      .setDescription("**You have 10 seconds to react on this message with the check emoji to participate ✅**")
      .setFooter("Good Luck! 🟢", "https://cdn.discordapp.com/emojis/892751786719469598.gif?size=56");

    List<Member> registrationHolder = new LinkedList<>();

    AtomicReference<Message> messageRef = new AtomicReference<>();
    ctx.channel.sendMessageEmbeds(embed.build()).queue(message -> {
      message.addReaction(Emoji.fromUnicode("✅")).queue();
      messageRef.set(message);
    });

    embed.setFooter("Good Luck! 🔴", "https://cdn.discordapp.com/emojis/892751786719469598.gif?size=56");

    waiter.watchFor(MessageReactionAddEvent.class, e -> e.getEmoji().getName().equals("✅"),
      event -> {
        registrationHolder.add(event.getMember());
        System.out.println(event.getUser().getName() + " reacted with " + event.getEmoji().getName());
        System.out.println("\n" + "Players: " + registrationHolder);
      }, 
      () -> {
        messageRef.get().editMessageEmbeds(embed.build()).queue();

        if (registrationHolder.size() < 2) {
          ctx.channel.sendMessage(registrationHolder.size() == 0 ? "No players registered, Game is closed." : "Sorry " + registrationHolder.get(0).getAsMention() + " but atleast two players are needed to inititate the Game.")
            .queue();
          return;
        }
    
        final List<Player> players = new ArrayList<>(registrationHolder.size());
        AtomicInteger lifes = new AtomicInteger(DEFULT_PLAYER_LIFES);
        
        StringBuilder builder = new StringBuilder();
        int ctr = 1;
    
        for (Member registeredMember : registrationHolder) {
          builder.append(ctr++);
          builder.append(". `");
          builder.append(registeredMember.getNickname());
          builder.append('`');
          builder.append('\n');
        }
    
        embed.setTitle("List of Players")
          .setDescription(builder.toString())
          .setFooter("Playability: " + getPlayability(players.size()));
    
        ctx.channel.sendMessageEmbeds(embed.build()).queue();
    
        waiter.waitFor(MessageReceivedEvent.class, 
          e -> fromSameAuthor(e, ctx) && fromSameServer(e, ctx) && validLifeSetResponse(e.getMessage().getContentRaw()),
          event -> lifes.set(Integer.parseInt(event.getMessage().getContentRaw())),
          () -> ctx.channel.sendMessage("Set player lifes to the default `3`.").queue(),
          5
        );
    
        registrationHolder.forEach(member -> players.add(new Player(member, lifes.get())));
    
        final Game game = new Game(players, ctx.channel);
        game.start();
      },
      10
    );

    waiter.watchFor(MessageReactionRemoveEvent.class, e  -> e.getEmoji().getName().equals("✅"),
      event -> {
        registrationHolder.remove(event.getMember());
        System.out.println(event.getUser().getName() + " removed their reaction " + event.getEmoji().getName());
        System.out.println("Players: " + registrationHolder + '\n');
      }, 10
    );
  }
}
