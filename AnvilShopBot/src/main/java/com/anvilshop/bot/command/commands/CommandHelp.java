package com.anvilshop.bot.command.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import com.anvilshop.bot.Bot;
import com.anvilshop.bot.command.Category;
import com.anvilshop.bot.command.Command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandHelp extends Command{
	public CommandHelp() {
		super("help", "help", "Help command.", Category.UTIL);
	}
	@Override
	public void onCommand(GuildMessageReceivedEvent event, String[] args) {
		MessageChannel channel = event.getChannel();
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setTitle("Help Command")
				.setColor(Color.ORANGE);
		HashMap<String[], ArrayList<Command>> categories = new HashMap<String[], ArrayList<Command>>();
		for(Command command : Bot.bot.commandInterpreter.commands) {
			if(!categories.containsKey(command._category())) {
				ArrayList<Command> cmdArray = new ArrayList<Command>();
				cmdArray.add(command);
				categories.put(command._category(), cmdArray);
			} else {
				ArrayList<Command> cmdArray = categories.get(command._category());
				cmdArray.add(command);
				categories.put(command._category(), cmdArray);
			}
		}
		for(String[] cat : categories.keySet()) {
			ArrayList<Command> catCommands = categories.get(cat);
			String list = "";
			for(Command command : catCommands)
				list += Bot.bot.commandInterpreter.prefix + command._usage() + " -> " + command._description() + "\n";
			embedBuilder.addField(cat[0], list, false);
		}
		embedBuilder.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl());
		channel.sendMessage(embedBuilder.build()).complete();
	}
}