package com.lucas.specterchat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.lucas.specterchat.chats.ChatComando;
import com.lucas.specterchat.chats.ChatFacComando;
import com.lucas.specterchat.chats.GComando;
import com.lucas.specterchat.chats.Local;
import com.lucas.specterchat.chats.RCommand;
import com.lucas.specterchat.chats.SComando;
import com.lucas.specterchat.chats.TellCommand;

public class Main extends JavaPlugin {

	public static Main getInstance() {
		return Main.getPlugin(Main.class);
	}

	public String status;
	@Override
	public void onEnable() {
		status = "DESMUTADO";
		getCommand("tell").setExecutor(new TellCommand());
		getCommand("r").setExecutor(new RCommand());
		getCommand("c").setExecutor(new ChatFacComando());
		getCommand("a").setExecutor(new ChatFacComando());
		getCommand("s").setExecutor(new SComando());
		getCommand("g").setExecutor(new GComando());
		getCommand("chat").setExecutor(new ChatComando());
		Bukkit.getServer().getPluginManager().registerEvents(new Local(), this);
	}
}