package com.anvilshop.bot.command.commands;

import java.awt.Color;

import com.anvilshop.bot.command.Category;
import com.anvilshop.bot.command.Command;
import com.anvilshop.bot.util.HTTPRequestUtil;
import com.anvilshop.bot.util.JSONUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandCheckQueue extends Command{
	public CommandCheckQueue() {
		super("checkqueue", "checkqueue", "Checks the queue of l2x9.", Category.UTIL);
	}
	@Override
	public void onCommand(GuildMessageReceivedEvent event, String[] args) {
		final GuildMessageReceivedEvent eventFinal = event;
		final MessageChannel channel = event.getChannel();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Message message = channel.sendMessage("Requesting server...").complete();
					JsonObject obj = JSONUtil.getJSON(HTTPRequestUtil.GETRequest("https://api.mcsrvstat.us/2/l2x9.org"));
					message.delete().queue();
					boolean online = obj.get("online").getAsBoolean();
					if(!online) {
						MessageEmbed embed = new EmbedBuilder()
								.setTitle("L2X9 Queue check")
								.setFooter("Made by PK2_Stimpy", eventFinal.getGuild().getIconUrl())
								.setColor(Color.RED)
								.setDescription("L2X9 is down.")
								.build();
						channel.sendMessage(embed).queue();
						return;
					}
					int players = JSONUtil.getJSONElement(obj, new String[] {"players", "online"}).getAsInt();
					EmbedBuilder embed = new EmbedBuilder()
							.setTitle("L2X9 Queue check")
							.setFooter("Made by PK2_Stimpy", eventFinal.getGuild().getIconUrl())
							.setColor(Color.BLACK)
							.setDescription("Players online: " + players);
					int queue = (players-100),
						prioq = 0;
					JsonElement info = JSONUtil.getJSONElement(obj, new String[] {"info"});
					if(info != null) {
						queue = Integer.parseInt(info.getAsJsonObject().get("clean").getAsJsonArray().get(2).getAsString().replaceAll("Regular: ", ""));
						prioq = Integer.parseInt(info.getAsJsonObject().get("clean").getAsJsonArray().get(1).getAsString().replaceAll("Priority: ", ""));
					}
					if(queue <= 0) embed.addField("Queue", "There isn't any player on the queue", false);
					else {
						embed.addField("Queue", "There are **" + queue + "** players on the queue.", false);
						if(prioq <= 0) embed.addField("Priority Queue", "There isn't any player on the priority queue", false);
						else embed.addField("Priority Queue", "There are **" + prioq + "** players on the priority queue", false);
					}
					/*
					if(players > 100) embed.addField("Queue", "There are **" + (players-100) + "** of queue. (Aproximate value)", false);
					else embed.addField("Queue", "There isn't any queue (Aproximate value)", false);
					*/
					channel.sendMessage(embed.build()).queue();
				} catch(Exception e) {
					MessageEmbed embed = new EmbedBuilder()
							.setTitle(":red_circle: There was an error! :red_circle:")
							.setDescription(":warning: " + e.getMessage() + " :warning:")
							.setFooter("Made by PK2_Stimpy", eventFinal.getGuild().getIconUrl())
							.setColor(Color.RED)
							.build();
					channel.sendMessage(embed).queue();
					e.printStackTrace();
					return;
				}
			}
		});
		thread.run();
	}
}