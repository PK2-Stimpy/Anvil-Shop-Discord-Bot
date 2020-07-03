package com.anvilshop.bot.scheduledtask.tasks;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.anvilshop.bot.Bot;
import com.anvilshop.bot.util.HTTPRequestUtil;
import com.anvilshop.bot.util.JSONUtil;
import com.google.gson.JsonObject;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class ASTUpdateL2X9Stats extends TimerTask{
	public String VCPlayersId  =  "728273644484165654",
		          VCQueueId    =  "728273655003611166";
	final Bot bot;
	final Guild guild;
	
	public ASTUpdateL2X9Stats(Guild guild) {
		bot = Bot.bot;
		this.guild = guild;
	}
	@Override
	public void run() {
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					VoiceChannel VCPlayers = guild.getVoiceChannelById(VCPlayersId);
					Thread.sleep(1000);
					VoiceChannel VCQueue   = guild.getVoiceChannelById(VCQueueId);
					JsonObject obj = JSONUtil.getJSON(HTTPRequestUtil.GETRequest("https://api.mcsrvstat.us/2/l2x9.org"));
					boolean online = obj.get("online").getAsBoolean();
					if(!online) {
						VCPlayers.getManager().setName("L2X9 is down.").complete();
						VCQueue.getManager().setName("L2X9 is down.").completeAfter(1, TimeUnit.SECONDS);
						return;
					} else {
						int players = JSONUtil.getJSONElement(obj, new String[] {"players", "online"}).getAsInt();
						VCPlayers.getManager().setName("Players: " + players).complete();
						String queueName = "There isn't queue (Aproximate)";
						if(players > 100) queueName = "Queue: " + (players-100) + " (Aproximate)";
						VCQueue.getManager().setName(queueName).completeAfter(1, TimeUnit.SECONDS);
					}
					System.out.println("UPDATED LIST!");
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.run();
	}
}