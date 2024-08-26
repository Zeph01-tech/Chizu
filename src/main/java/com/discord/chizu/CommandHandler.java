package com.discord.chizu;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.discord.utilities.HelperFuncs;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHandler {
  public final Map<String, Command> commandsThroughNames = new HashMap<>();
  public final Map<String, Command> commandsThroughAliases = new HashMap<>();
  public String[] prefixes;
  public static final String adminId = "762372102204030986"; 
  public static final String adminServerId = "762380604058632222";
  public static Guild adminServer;

  public CommandHandler build() throws 
  InstantiationException, 
  IllegalAccessException, 
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

    return this;
  }

  public void register(Command cmd) {
    this.commandsThroughNames.put(cmd.name, cmd);
    
    cmd.aliases.forEach(alias -> this.commandsThroughAliases.put(alias, cmd));
  }

  public CommandHandler setPrefixes(String[] prefixes) {
    this.prefixes = prefixes;
    return this;
  }

  private static Class<?> getClass(String className, String packageName) {

    try {
      return Class.forName(packageName + "." + className);
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public void execute(MessageReceivedEvent event) {
    String[] args = event.getMessage().getContentRaw().split(" ");

    // boolean valid = HelperFuncs.hasValue(prefixes, args[0]);
    // boolean valid = false;
    // for (String prefix : prefixes)
    //   if (prefix.equals(args[0]))
    //     valid = true;

    if (!HelperFuncs.hasValue(prefixes, args[0])) return;

    if (this.commandsThroughNames.containsKey(args[1]))
      executeCommand(this.commandsThroughNames.get(args[1]), new Context(event, args));

    else if (this.commandsThroughAliases.containsKey(args[1]))
      executeCommand(this.commandsThroughAliases.get(args[1]), new Context(event, args));

  }

  private static void executeCommand(Command cmd, Context ctx) {
    if (!cmd.adminOnly)
      cmd.execute(ctx);

    else if (ctx.authorId.equals(adminId))
      cmd.execute(ctx);
    
    else
      ctx.channel.sendMessage("Command can only be invoked by bot Owner");

  }
}
