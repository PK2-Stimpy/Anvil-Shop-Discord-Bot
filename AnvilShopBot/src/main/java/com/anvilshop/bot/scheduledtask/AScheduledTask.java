package com.anvilshop.bot.scheduledtask;

import java.util.ArrayList;
import java.util.Timer;

import com.anvilshop.bot.scheduledtask.tasks.ASTUpdateL2X9Stats;

import net.dv8tion.jda.core.entities.Guild;

public class AScheduledTask {
	static ArrayList<Guild> guilds = new ArrayList<Guild>();
	public static void initScheduledTasks(Guild guild) {
		Timer time = new Timer();
		if(guilds.contains(guild)) return;
		/*ASTUpdateL2X9Stats updl2x9stats = new ASTUpdateL2X9Stats(guild);
		time.schedule(updl2x9stats, 0, 500000);*/
		
		guilds.add(guild);
	}
}