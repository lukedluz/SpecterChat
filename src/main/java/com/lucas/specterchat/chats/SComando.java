package com.lucas.specterchat.chats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class SComando implements CommandExecutor {

	public static String getMensagem(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lb, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (cmd.getName().equalsIgnoreCase("s")) {
				if(!p.hasPermission("hubchat.staffchat")) {
					p.sendMessage("�cVoc� n�o tem permiss�o para fazer isso.");
					return true;
				}
				if (args.length == 0) {
					p.sendMessage("�cUse /s <mensagem>.");
					return true;
				}
				if (args.length >= 1) {
					for(Player on : Bukkit.getOnlinePlayers()) {
						if(on.hasPermission("hubchat.staffchat")) {
							String prefixcomnome = PermissionsEx.getUser(p).getPrefix().replace("&", "�") + p.getName();
							on.sendMessage("�d[Chat da Staff] " + prefixcomnome + ": �f" + getMensagem(args));
						}
					}
				}
			}
		}
		return true;
	}

}
