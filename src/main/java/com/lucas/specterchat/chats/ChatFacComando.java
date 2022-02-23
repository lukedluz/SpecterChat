package com.lucas.specterchat.chats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

public class ChatFacComando implements CommandExecutor {

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
			MPlayer mp = MPlayer.get(p);
			if (cmd.getName().equalsIgnoreCase("c")) {
				if (args.length == 0) {
					p.sendMessage("�cUse /c <mensagem>.");
					return true;
				}
				if (args.length >= 1) {
					if (!mp.hasFaction()) {
						p.sendMessage("�cVoc� n�o faz parte de uma fac��o.");
						return true;
					}
					String msg = getMensagem(args);
					for (Player todos : Bukkit.getOnlinePlayers()) {
						MPlayer todosm = MPlayer.get(todos);
						if (todosm.getFaction().equals(mp.getFaction())) {
							todos.sendMessage("�e" + mp.getRole().getPrefix() + "�f" + p.getName() + "�7: " + msg);
						}
					}

					return true;
				}
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("a")) {
				if (args.length == 0) {
					p.sendMessage("�cUse /a <mensagem>.");
					return true;
				}
				if (args.length >= 1) {
					if (!mp.hasFaction()) {
						p.sendMessage("�cVoc� n�o faz parte de uma fac��o.");
						return true;
					}
					Faction fac = mp.getFaction();
					String msg = getMensagem(args);
					for (Player todos : Bukkit.getOnlinePlayers()) {
						Faction fac2 = MPlayer.get(todos).getFaction();
						if (fac.getRelationWish(fac2).equals(Rel.ALLY)) {
							todos.sendMessage("�6[Chat de aliados] �7[" + mp.getRole().getPrefix() + fac.getName()
									+ "] �f" + p.getName() + "�3: " + msg);
						}
						if (fac2.equals(fac)) {
							todos.sendMessage("�6[Chat de aliados] �7[" + mp.getRole().getPrefix() + fac.getName()
									+ "] �f" + p.getName() + "�3: " + msg);
						}
					}
				}
			}
		}
		return true;
	}

}
