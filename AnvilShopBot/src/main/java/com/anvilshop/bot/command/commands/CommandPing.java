package com.anvilshop.bot.command.commands;

import com.anvilshop.bot.command.Category;
import com.anvilshop.bot.command.Command;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandPing extends Command{
	public CommandPing() {
		super("ping", "ping [message]", "Tests if the bot is working fine.", Category.MODERATION);
	}
	@Override
	public void onCommand(GuildMessageReceivedEvent event, String[] args) {
		MessageChannel channel = event.getChannel();
		if(!hasPermission(event.getMember())) return;
		String msg = "Pong!";
		StringBuilder builder = new StringBuilder();
		for(int i = 1; i < args.length; i++) {
			builder.append(args[i]);
			if(i != args.length) builder.append("\n");
		}
		if(args.length > 1) msg = builder.toString();
		channel.sendMessage(event.getAuthor().getAsMention() + " " + msg).complete();
	}
}