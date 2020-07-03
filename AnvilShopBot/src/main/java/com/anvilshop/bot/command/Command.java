package com.anvilshop.bot.command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class Command {
	private String label, usage, description;
	private String[] category;
	public Command(String label, String usage, String description, String[] category) {
		this.label = label;
		this.usage = usage;
		this.description = description;
		this.category = category;
	}
	public void onCommand(GuildMessageReceivedEvent event, String[] args) throws IllegalArgumentException {}
	public void onReactAdd(GuildMessageReactionAddEvent event) {}
	public void onReactRem(GuildMessageReactionRemoveEvent event) {}
	public String _label() {return label;}
	public String _usage() {return usage;}
	public String _description() {return description;}
	public String[] _category() {return category;}
	public boolean hasPermission(Member member) {
		boolean canExec = false;
		for(Role role : member.getRoles()) 
			if(category[1] != null)
				for(int i = 1; i < category.length; i++)
					if(role.getName().equalsIgnoreCase(category[i]))
						canExec = true;
		return canExec;
	}
}