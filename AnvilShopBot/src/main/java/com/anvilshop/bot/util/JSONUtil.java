package com.anvilshop.bot.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONUtil {
	@SuppressWarnings("deprecation")
	public static JsonObject getJSON(String _json) {
		return new JsonParser().parse(_json).getAsJsonObject();
	}
	public static JsonElement getJSONElement(JsonObject json, String[] path) throws Exception {
		JsonElement element = json.get(path[0]);
		if(path.length > 1) for(int i = 1; i < path.length; i++) element = element.getAsJsonObject().get(path[i]);
		return element;
	}
}	