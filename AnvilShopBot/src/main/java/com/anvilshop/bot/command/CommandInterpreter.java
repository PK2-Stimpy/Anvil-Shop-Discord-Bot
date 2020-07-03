package com.anvilshop.bot.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.anvilshop.bot.command.commands.CommandAddProduct;
import com.anvilshop.bot.command.commands.CommandCheckQueue;
import com.anvilshop.bot.command.commands.CommandClear;
import com.anvilshop.bot.command.commands.CommandGiveaway;
import com.anvilshop.bot.command.commands.CommandHelp;
import com.anvilshop.bot.command.commands.CommandPing;
import com.anvilshop.bot.command.commands.CommandServer;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;


public class CommandInterpreter {
	public ArrayList<Command> commands;
	public String prefix;
	public CommandInterpreter() { 
		prefix = "--";
		
		commands = new ArrayList<Command>();
		commands.add(new CommandHelp());
		commands.add(new CommandServer());
		commands.add(new CommandCheckQueue());
		
		commands.add(new CommandAddProduct());
		
		commands.add(new CommandClear());
		commands.add(new CommandGiveaway());
		commands.add(new CommandPing());
	}
	public Command getCommand(String label) {
		for(Command command : commands)
			if(command._label().equalsIgnoreCase(label))
				return command;
		return null;
	}
	public boolean handleCommand(GuildMessageReceivedEvent e) {
		String[] args = e.getMessage().getContentRaw().split(" ");
		if(!args[0].startsWith(prefix)) return false;
		args[0] = args[0].replaceFirst("--", "");
		if(getCommand(args[0]) == null) return false;
		Command command = getCommand(args[0]);
		try {
			command.onCommand(e, args);
		} catch(IllegalArgumentException illegalArgException) {
			TextChannel channel = e.getChannel();
			MessageEmbed embed = new EmbedBuilder()
					.setTitle(":red_circle: Incorrect usage :red_circle:")
					.setDescription(" :warning: Usage: ` " + prefix + command._usage() + " ` :warning: ")
					.setFooter("Made by PK2_Stimpy", e.getGuild().getIconUrl())
					.setColor(Color.RED)
					.build();
			Message msg = channel.sendMessage(embed).complete();
			msg.delete().queueAfter(5, TimeUnit.SECONDS);
			return false;
		}
		return true;
	}
	public void handleReaction(Object event, int type) {
		for(Command command : commands)
			switch (type) {
			case 0:
				command.onReactRem((GuildMessageReactionRemoveEvent)event);
				break;
			case 1:
				command.onReactAdd((GuildMessageReactionAddEvent)event);
				break;
			default:
				break;
			}
	}
}