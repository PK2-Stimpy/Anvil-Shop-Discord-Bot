package com.anvilshop.bot;

import javax.security.auth.login.LoginException;

import com.anvilshop.bot.command.CommandInterpreter;
import com.anvilshop.bot.httpstatus.HttpServerTest;
import com.anvilshop.bot.scheduledtask.AScheduledTask;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;



public class Bot extends ListenerAdapter{
	public CommandInterpreter commandInterpreter = new CommandInterpreter();
	public static Bot bot;
	public static JDA jda;
	public Bot() {
		bot = this;
	}
	public void sendMsg(String message, MessageChannel channel) {
		channel.sendMessage(message);
	}
	public void delMsg(String messageId, MessageChannel channel) {
		channel.deleteMessageById(messageId);
	}
	
	public static void main(String[] args) throws LoginException {
		HttpServerTest.run();
		
		String token = "";
		jda = new JDABuilder(AccountType.BOT)
				.setToken(token)
				.addEventListener(new Bot())
				.build();
		System.out.println("Logging in with:\n"
				+ "   - Token == " + token + "\n");
		jda.getPresence().setGame(Game.watching("--help"));
	}
	public void onGuildReady(GuildReadyEvent event) {
		if(event.getGuild().getId().equalsIgnoreCase("700774696521695353")) AScheduledTask.initScheduledTasks(event.getGuild());
	}
	public void onReady(ReadyEvent e) {
		e.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
		System.out.println("Bot is ready to function!");
	}
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if(e.getAuthor().isBot()) return;
		System.out.println("Got a message, " + commandInterpreter.handleCommand(e));
		
	}
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
		if(e.getUser().isBot()) return;
		commandInterpreter.handleReaction(e, 1);
		System.out.println("react.add");
	}
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent e) { 
		if(e.getUser().isBot()) return;
		commandInterpreter.handleReaction(e, 0);
		System.out.println("react.remove");
	}
}