package com.anvilshop.bot.command.commands;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.anvilshop.bot.command.Category;
import com.anvilshop.bot.command.Command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandAddProduct extends Command{
	public CommandAddProduct() {
		super("addproduct", "addproduct <name> <pricepp> <priceinv> <imageurl>", "Adds a product.", Category.MANAGEMENT);
	}
	@Override
	public void onCommand(GuildMessageReceivedEvent event, String[] args) {
		MessageChannel channel = event.getChannel();
		Member member = event.getMember();
		if(!hasPermission(member)) return;
		if(args.length < 5) throw new IllegalArgumentException();
		try {
			String name = args[1].replaceAll("%20", " ");
			String pricepp = args[2].replaceAll("%20", " ");
			String priceinv = args[3].replaceAll("%20", " ");
			String imageurl = args[4].replaceAll("%20", " ");
			event.getMessage().delete().complete();
			EmbedBuilder embedBuilder = new EmbedBuilder()
					.setTitle(name.toUpperCase())
					.addField("Paypal: ", pricepp, true)
					.addField("Invites: ", priceinv, true)
					.setImage(imageurl)
					.setColor(Color.MAGENTA)
					.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl());
			/*channel.sendMessage("**" + name.toUpperCase() + "**\n"
					+ "Paypal: " + pricepp + "\n"
					+ "Invites: " + priceinv);*/
			channel.sendMessage(embedBuilder.build()).complete();
		} catch(IllegalArgumentException e) {
			MessageEmbed embed = new EmbedBuilder()
					.setTitle(":red_circle: There was an error! :red_circle:")
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