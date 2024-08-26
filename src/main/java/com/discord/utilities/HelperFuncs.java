package com.discord.utilities;

import java.util.Random;

import com.discord.chizu.Context;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static com.discord.chizu.CommandHandler.adminServer;

import java.util.List;

public final class HelperFuncs {
  private static Random randomStream = new Random();

  public static int randint(int bound) {
    return randomStream.nextInt(bound);
  }

  public static int randint() {
    return randomStream.nextInt();
  }

  public static boolean fromSameAuthor(MessageReceivedEvent e, Context ctx) {
    return e.getAuthor().getId().equals(ctx.authorId);
  }

  public static boolean fromSameServer(MessageReceivedEvent e, Context ctx) {
    return e.getChannel().getId().equals(ctx.channelId);
  }

  public static String getEmoji(String emojiName) {
    var result = adminServer.getEmojisByName(emojiName, false);
    if (result.size() == 0) return "<emoji-not-found>";

    return result.get(0).getAsMention();
  } 

  public static <T> T random_choice(List<T> list) {
    return list.get(randomStream.nextInt(list.size() - 1));
	}

  public static String random_choice(String[] arr) {
		return arr[randomStream.nextInt(arr.length-1)];
	}

  public static boolean hasValue(List<String> list, String value) {
		for (String str : list)
			if (str.equals(value))
				return true;

		return false;

  }
  
  public static boolean hasValue(String[] arr, String value) {
		for (String str : arr)
			if (str.equals(value))
				return true;

		return false;
	}

	public static boolean hasValueIgnoreCase(String[] arr, String value) {
		for (String str : arr)
			if (str.equalsIgnoreCase(value))
				return true;

		return false;
	}

	public static String mergearr(String[] arr) {
		String out = "";
		for (String str : arr)
			out += str + " ";

		return out; 
	}
}
