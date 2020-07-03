package com.anvilshop.bot.command.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.anvilshop.bot.command.Category;
import com.anvilshop.bot.command.Command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class CommandGiveaway extends Command{
	public HashMap<String, ArrayList<String>> giveaways = new HashMap<String, ArrayList<String>>();
	
	public CommandGiveaway() {
		super("giveaway", "giveaway <name> <price>", "Host a giveaway.", Category.MODERATION);
	}
	@Override
	public void onCommand(GuildMessageReceivedEvent event, String[] args) throws IllegalArgumentException {
		Member member = event.getMember();
		TextChannel channel = event.getChannel();
		if(!hasPermission(member)) return;
		event.getMessage().delete().queue();
		if(args.length < 3) throw new IllegalArgumentException();
		String description = args[1].replaceAll("%20", " ");
		String price = args[2].replaceAll("%20", " ");
		MessageEmbed embed = new EmbedBuilder()
				.setTitle("Giveaway")
				.setDescription(description)
				.addField("Price", price, true)
				.addField("How do I participate?", "React with ✅  to participate", true)
				.addField("Hosted by: ", member.getUser().getName() + "#" + member.getUser().getDiscriminator(), false)
				.setColor(Color.GREEN)
				.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl())
				.build();
		Message message = channel.sendMessage(embed).complete();
		message.addReaction("✅").queue();
		message.addReaction("❌").queue();
		giveaways.put(message.getId(), new ArrayList<String>());
	}
	
	@Override
	public void onReactAdd(GuildMessageReactionAddEvent event) {
		Member member = event.getMember();
		String messageId = event.getMessageId();
		Message message = event.getChannel().getMessageById(messageId).complete();
		String reaction = event.getReaction().getReactionEmote().getName();
		if(!giveaways.containsKey(messageId)) return;
		// YES
		if(reaction.equals("✅")) {
			ArrayList<String> array = giveaways.get(messageId);
			array.add(member.getUser().getId());
			giveaways.put(messageId, array);
		}
		// REMOVE
		if(reaction.equals("❌")) {
			if(!hasPermission(member)) return;
			ArrayList<String> userIds = giveaways.get(messageId);
			String winner = "NO";
			if(userIds.size() >= 1) {
				int sel = new Random().nextInt(userIds.size());
				User user = event.getGuild().getMemberById(userIds.get(sel)).getUser();
				winner = user.getName() + "#" + user.getDiscriminator();
				event.getChannel().sendMessage(user.getAsMention()).queue();
			}
			giveaways.remove(messageId);
			String desc = message.getEmbeds().get(0).getDescription();
			message.delete().queue();
			MessageEmbed embed = new EmbedBuilder()
					.setTitle("Giveaway ENDED")
					.setDescription(desc)
					.addField("Winner: ", winner, true)
					.setColor(Color.GREEN)
					.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl())
					.build();
			event.getChannel().sendMessage(embed).queue();
		}
	}
	@Override
	public void onReactRem(GuildMessageReactionRemoveEvent event) {
		Member member = event.getMember();
		String messageId = event.getMessageId();
		String reaction = event.getReaction().getReactionEmote().getName();
		if(!giveaways.containsKey(messageId)) return;
		if(reaction.equals("✅")) {
			ArrayList<String> userIds = giveaways.get(messageId);
			if(userIds.contains(member.getUser().getId()))
				giveaways.remove(member.getUser().getId());
			
			giveaways.put(messageId, userIds);
		}
	}
}