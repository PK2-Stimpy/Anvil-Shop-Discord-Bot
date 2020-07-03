package com.anvilshop.bot.command.commands;

import java.awt.Color;

import org.apache.commons.codec.binary.Base64;

import com.anvilshop.bot.command.Category;
import com.anvilshop.bot.command.Command;
import com.anvilshop.bot.util.HTTPRequestUtil;
import com.anvilshop.bot.util.JSONUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class CommandServer extends Command{
	public CommandServer() {
		super("server", "server <ip>", "Ping the server and returns the response.", Category.UTIL);
	}
	
	private void jsonUnpack(GuildMessageReceivedEvent event, String[] args, JsonObject obj) throws Exception {
		EmbedBuilder build = new EmbedBuilder()
				.setTitle("Server Info")
				.setDescription("IP: `" + args[1] + "`")
				.setFooter("Made by PK2_Stimpy", event.getGuild().getIconUrl())
				.setColor(Color.BLACK);
		boolean online = obj.get("online").getAsBoolean();
		String ip = obj.get("ip").getAsString();
		int port = JSONUtil.getJSONElement(obj, new String[]{"port"}).getAsInt();
		boolean debug_ping = JSONUtil.getJSONElement(obj, new String[]{"debug","ping"}).getAsBoolean(),
				debug_query = JSONUtil.getJSONElement(obj, new String[]{"debug","query"}).getAsBoolean(),
				debug_srv = JSONUtil.getJSONElement(obj, new String[]{"debug","srv"}).getAsBoolean(),
				debug_querymismatch = JSONUtil.getJSONElement(obj, new String[]{"debug","querymismatch"}).getAsBoolean(),
				debug_ipinsrv = JSONUtil.getJSONElement(obj, new String[]{"debug","ipinsrv"}).getAsBoolean(),
				debug_cnameinsrv = JSONUtil.getJSONElement(obj, new String[]{"debug","cnameinsrv"}).getAsBoolean(),
				debug_animatedmotd = JSONUtil.getJSONElement(obj, new String[]{"debug","animatedmotd"}).getAsBoolean();
		int debug_cachetime = JSONUtil.getJSONElement(obj, new String[]{"debug","cachetime"}).getAsInt();
		build.addField("Online", ""+online, false)
			.addField("IP", ip, true)
			.addField("Port", ""+port, false)
			.addBlankField(false)
			.addField("Debug","",false)
			.addField("debug_ping", ""+debug_ping, true)
			.addField("debug_query", ""+debug_query, true)
			.addField("debug_srv", ""+debug_srv, false)
			.addField("debug_querymismatch", ""+debug_querymismatch, true)
			.addField("debug_ipinsrv", ""+debug_ipinsrv, true)
			.addField("debug_cnameinsrv", ""+debug_cnameinsrv, false)
			.addField("debug_animatedmotd", ""+debug_animatedmotd, true)
			.addField("debug_cachetime", ""+debug_cachetime, true);
		if(online) {
			build.setColor(Color.GREEN);
			int players_online = JSONUtil.getJSONElement(obj, new String[] {"players", "online"}).getAsInt(),
					players_max = JSONUtil.getJSONElement(obj, new String[] {"players", "max"}).getAsInt();
			// JsonArray players_list = JSONUtil.getJSONElement(obj, new String[] {"players","list"}).getAsJsonArray();
			build.setDescription("IP: `" + ip + "` *" + players_online + "/" + players_max + "*");
			JsonArray _motd = JSONUtil.getJSONElement(obj, new String[] {"motd","clean"}).getAsJsonArray();
			String motd = _motd.get(0).getAsString();
			if(_motd.size() > 1) motd+="\n"+_motd.get(1).getAsString();
			build.addBlankField(false)
				.addField("Motd", motd, false)
				.addBlankField(false)
				.addField("Version: ", JSONUtil.getJSONElement(obj, new String[] {"version"}).getAsString(), true);
			JsonElement protocol = JSONUtil.getJSONElement(obj, new String[] {"protocol"});
			if(protocol != null) build.addField("Protocol: ", ""+protocol.getAsInt(), true);
			JsonElement hostname = JSONUtil.getJSONElement(obj, new String[]{"hostname"});
			if(hostname != null) build.addField("Hostname: ", hostname.getAsString(), false);
			JsonElement software = JSONUtil.getJSONElement(obj, new String[] {"software"});
			if(software != null) build.addField("Software: ", software.getAsString(), true);
			JsonElement map = JSONUtil.getJSONElement(obj, new String[] {"map"});
			if(map != null) build.addField("Map: ", map.getAsString(), true);
			JsonElement _info = JSONUtil.getJSONElement(obj, new String[] {"info"});
			if(_info != null) {
				_info = _info.getAsJsonObject().get("clean");
				JsonArray arr = _info.getAsJsonArray();
				String info = "";
				info = arr.get(0).getAsString();
				for(int i = 1; i < arr.size(); i++) info+="\n"+arr.get(i).getAsString();
				build.addField("Info: ", info, false);
			}
			
			
			MessageAction message = event.getChannel().sendMessage(build.build());
			JsonElement _icon = JSONUtil.getJSONElement(obj, new String[] {"icon"});
			if(_icon != null) {
				String icon = _icon.getAsString();
				String imageData = icon.substring(icon.indexOf(",")+1);
				byte[] imageBytes = Base64.decodeBase64(imageData.getBytes());
				message.addFile(imageBytes, "lolkurva.png");
			}
			
			message.queue();
			return;
		}
		build.setColor(Color.RED);
		JsonElement hostname = JSONUtil.getJSONElement(obj, new String[]{"hostname"});
		if(hostname != null) build.addBlankField(false).addField("Hostname: ", hostname.getAsString(), false);
		event.getChannel().sendMessage(build.build()).queue();
	}
	
	@Override
	public void onCommand(GuildMessageReceivedEvent event, String[] args) {
		final GuildMessageReceivedEvent eventFinal = event;
		final String[] argsFinal = args;
		final MessageChannel channel = event.getChannel();
		if(args.length < 2) throw new IllegalArgumentException();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Message message = channel.sendMessage("Requesting server...").complete();
					JsonObject json = JSONUtil.getJSON(HTTPRequestUtil.GETRequest("https://api.mcsrvstat.us/2/" + argsFinal[1]));
					message.delete().queue();
					jsonUnpack(eventFinal, argsFinal, json);
				} catch(Exception e) {
					MessageEmbed embed = new EmbedBuilder()
							.setTitle(":red_circle: There was an error! :red_circle:")
							.setDescription(":warning: " + e.getMessage() + " :warning:")
							.setFooter("Made by PK2_Stimpy", eventFinal.getGuild().getIconUrl())
							.setColor(Color.RED)
							.build();
					channel.sendMessage(embed).queue();
					e.printStackTrace();
					return;
				}
			}
		});
		thread.run();
	}
}