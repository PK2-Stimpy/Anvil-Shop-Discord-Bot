package com.anvilshop.bot.command;

public interface Category {
	String[] UTIL = {"Util Commands", "User", "anvil_mod", "anvil_admin", "anvil_owner"},
			FUN = {"Fun Commands", "User", "anvil_mod", "anvil_admin", "anvil_owner"},
			MODERATION = {"Moderation Commands", "anvil_mod", "anvil_admin", "anvil_owner"},
			ADMINISTRATION = {"Administration Commands", "anvil_admin", "anvil_owner"},
			OWNER = {"Owner Commands", "anvil_owner"},
			MANAGEMENT = {"Management Commands", "anvil_admin", "anvil_owner"};
}