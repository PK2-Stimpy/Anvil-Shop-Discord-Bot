package com.anvilshop.bot.command.commands;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.anvilshop.bot.command.Category;
import com.anvilshop.bot.command.Command;
import com.anvilshop.bot.util.MathUtil;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandClear extends Command{
	public CommandClear() {
		super("clear", "clear <messages>", "Clear the last messages sent.", Category.MODERATION);
	}
	@Override
	public void onCommand(GuildMessageReceivedEvent event, String[] args) {
		Member member = event.getMember();
		TextChannel channel = event.getChannel();
		if(!hasPermission(member)) return;
		event.getMessage().delete().queue();
		if(args.length < 2) throw new IllegalArgumentException();
		boolean isInteger = MathUtil.isNumeric(args[1]);
		if(!isInteger) {
			MessageEmbed embed = new EmbedBuilder()
					.setTitle(":red_circle: That's not an integer! :red_circle:")
					.setDescription(" :warning: You need to put an number, not an string. :warning: ")
					.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl())
					.setColor(Color.RED)
					.build();
			Message msg = channel.sendMessage(embed).complete();
			msg.delete().queueAfter(5, TimeUnit.SECONDS);
			return;
		}
		int numMsg = Integer.parseInt(args[1]);
		if(numMsg > 100 || numMsg < 3) {
			MessageEmbed embed = new EmbedBuilder()
					.setTitle(":red_circle: Too many messages selected! :red_circle:")
					.setDescription(" :warning: You exceded the max messages you can input. (MAX = 100, MIN = 2) :warning: ")
					.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl())
					.setColor(Color.RED)
					.build();
			Message msg = channel.sendMessage(embed).complete();
			msg.delete().queueAfter(5, TimeUnit.SECONDS);
			return;
		}
		try {
			channel.deleteMessages(channel.getHistory().retrievePast(numMsg).complete()).queue();
			MessageEmbed embed = new EmbedBuilder()
					.setTitle(":green_circle: Messages deleted! :green_circle:")
					.setDescription(" :warning: I have deleted " + numMsg + " messages. :warning: ")
					.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl())
					.setColor(Color.GREEN)
					.build();
			Message msg = channel.sendMessage(embed).complete();
			msg.delete().queueAfter(5, TimeUnit.SECONDS);
			return;
		} catch(IllegalArgumentException e) {
			MessageEmbed embed = new EmbedBuilder()
					.setTitle(":red_circle: There was an error deleting messages! :red_circle:")
					.setDescription(":warning: " + e.getMessage() + " :warning:")
					.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl())
					.setColor(Color.RED)
					.build();
			Message msg = channel.sendMessage(embed).complete();
			msg.delete().queueAfter(5, TimeUnit.SECONDS);
			return;
		}
	}
}