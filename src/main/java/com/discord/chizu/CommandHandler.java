package com.discord.chizu;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHandler {
  public List<Command> commands = new ArrayList<>();
  public String[] prefixes;
  static long adminId = 762372102204030986L; 

  private CommandHandler(@Nullable String _null) throws 
  InstantiationException, 
  IllegalAccessException, 
  IllegalArgumentException, 
  InvocationTargetException, 
  NoSuchMethodException, 
  SecurityException {

    File[] files = new File("./src/main/java/com/discord/chizu/Commands").listFiles();

    for (File file : files) {
      String className = file.getName();
      className = className.substring(0, className.lastIndexOf("."));
      Class<?> command = getClass(className, "com.discord.chizu.Commands");
      command.getConstructor().newInstance();
    }
  }

  private CommandHandler() {}

  public static CommandHandler construct() {
    return new CommandHandler();
  }

  public CommandHandler build() throws Exception {
    new CommandHandler(null);
    return this;
  }

  public void register(Command cmd) {
    this.commands.add(cmd);
  }

  public CommandHandler setPrefixes(String[] prefixes) {
    this.prefixes = prefixes;
    return this;
  }

  private static Class<?> getClass(String className, String packageName) {
    try {
      return Class.forName(packageName + "."+ className);
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public void execute(MessageReceivedEvent event) {
    String[] args = event.getMessage().getContentRaw().split(" ");
    boolean valid = false;

    for (String prefix : prefixes)
      if (prefix.equals(args[0]))
        valid = true;

    if (!valid) return;

    for (Command command : commands) {
      if (command.name.equals(args[1]) || command.isAlias(args[1])) {
        Context ctx = new Context(event, args);

        if (!command.adminOnly) {
          command.execute(ctx);
          return;
        }

        if (ctx.authorId == adminId)
          command.execute(ctx);
        else
          ctx.channel.sendMessage("Command can only be invoked by botOwner.");

        return;
      }
    }
  }
}
