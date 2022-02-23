package com.lucas.specterchat.chats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.hubtoggle.Main;
import net.hubtoggle.object.Toggle;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class RCommand implements CommandExecutor {

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
			if (cmd.getName().equalsIgnoreCase("r")) {
				if (args.length == 0) {
					p.sendMessage("�cUse /r <mensagem>.");
					return true;
				}
				if(args.length == 1) {
					if(!TellCommand.responder.containsKey(p)) {
						p.sendMessage("�cVoc� n�o tem um jogador para responder.");
						return true;
					}
					if(TellCommand.responder.get(p) == null) {
						p.sendMessage("�cO jogador que voc� ia responder se encontra offline!");
						return true;
					}
					Toggle t = Main.instance.cache.get(TellCommand.responder.get(p).getName());
					if(!t.isTell()) {
						p.sendMessage("�cEste jogador est� com o tell desativado.");
						return true;
					}
					Player alvo = TellCommand.responder.get(p);
					String mensagem = getMensagem(args);
					TellCommand.way(p, "�eMensagem para �f" + alvo.getName() + "�7: " + mensagem, new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("�6Clique para responder.").create()), new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + alvo.getName() + " "));
					TellCommand.way(alvo, "�eMensagem de �f" + p.getName() + "�7: " + mensagem, new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("�6Clique para responder.").create()), new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + p.getName() + " "));
					return true;
				}
			}
		}
		return true;
	}

}
