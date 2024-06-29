package com.discord.chizu.Commands;

import java.io.IOException;

import com.discord.chizu.*;

import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Google extends Command {
  public Google() {
    super("Google", "Fetches icon from `www.google.com`", new String[] {"google"}, false);

    this.register();
    System.out.println("Google Registered");
  }

  @Override
  public void execute(Context ctx) {
    if (ctx.args.length != 0) return;
    EmbedBuilder embed = new EmbedBuilder();

    String icon = fetchData();
    if (icon == null) {
      ctx.channel.sendMessageEmbeds(
            embed.setTitle("Error occured")
            .setDescription("An error occurred either in the network call or in the html extraction.")
            .setColor(0xe0ddd7)
            .build()
      ).queue();
    }

    else {
      embed.setTitle("**Here you go**")
      .setImage("https://www.google.com" + icon)
      .setColor(0xe0ddd7);
      ctx.channel.sendMessageEmbeds(embed.build()).queue();
    }
  }

  private String fetchData() {
    try {
      Request req = new Request.Builder().url("https://www.google.com").build();
      OkHttpClient client = new OkHttpClient();

      Response res = client.newCall(req).execute();
      String html = res.body().string();
      int alt_tag = html.indexOf("alt=\"Google\"");
      String url = html.substring(html.indexOf("src=", alt_tag)+ 5, html.indexOf(".png", alt_tag) + 4);

      return url;
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return null;
    } catch (Error e) {
      System.out.println(e.getMessage());
      return "Bro stfu";
    }
  }
}
