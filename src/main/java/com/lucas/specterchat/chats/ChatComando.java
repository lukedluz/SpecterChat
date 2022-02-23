package com.lucas.specterchat.chats;

import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatComando implements CommandExecutor {

	private void enviarMensagem(int cu) {
		Bukkit.getOnlinePlayers().forEach(on -> on.sendMessage(" "));
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lb, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (cmd.getName().equalsIgnoreCase("chat")) {
				if (!p.hasPermission("hubchat.chat")) {
					p.sendMessage("�cVoc� n�o tem permiss�o para fazer isso.");
					return true;
				}
				if (args.length == 0) {
					p.sendMessage(" ");
					p.sendMessage("�e/chat limpar �fLimpar o chat.");
					p.sendMessage("�e/chat mutar �fMutar o canal Global e Local.");
					p.sendMessage("�e/chat desmutar �fDesmutar o canal Global e Local.");
					p.sendMessage(" ");
					return true;
				}
				if (args.length >= 2) {
					p.sendMessage("�cVoc� n�o pode inserir espa�os aqui.");
					return true;
				}
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("limpar")) {
						IntStream.rangeClosed(0, 300).forEach(this::enviarMensagem);
					} else if (args[0].equalsIgnoreCase("mutar")) {
						if (com.lucas.specterchat.Main.getInstance().status.equals("MUTADO")) {
							p.sendMessage("�cO chat j� se encontra mutado.");
							return true;
						}
						com.lucas.specterchat.Main.getInstance().status = "MUTADO";
						p.sendMessage("�eChat mutado com sucesso.");
					} else if (args[0].equalsIgnoreCase("desmutar")) {
						if (com.lucas.specterchat.Main.getInstance().status.equals("DESMUTADO")) {
							p.sendMessage("�cO chat j� se encontra desmutado.");
							return true;
						}
						com.lucas.specterchat.Main.getInstance().status = "DESMUTADO";
						p.sendMessage("�eChat desmutado com sucesso.");
					} else {
						p.sendMessage("�cSub comando n�o encontrado.");
					}
					return true;
				}
			}
		}
		return true;

	}
}
