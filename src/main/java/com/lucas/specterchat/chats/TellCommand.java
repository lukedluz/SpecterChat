package com.lucas.specterchat.chats;

import java.util.HashMap;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TellCommand implements CommandExecutor {
	public static String getMensagem(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}

	public static HashMap<Player, Player> responder = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lb, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (cmd.getName().equalsIgnoreCase("tell")) {
				if (args.length == 0) {
					p.sendMessage("�cUse /tell <jogador> <mensagem>.");
					return true;
				}
				if (args.length == 1) {
					Player alvo = Bukkit.getPlayer(args[0]);
					if (alvo == null) {
						p.sendMessage("�cEste jogador se encontra offline!");
						return true;
					}
					if (alvo.getName().equals(p.getName())) {
						p.sendMessage("�cEste jogador � voc�!");
						return true;
					}
					Toggle t = Main.instance.cache.get(alvo.getName());
					if(!t.isTell()) {
						p.sendMessage("�cEste jogador est� com o tell desativado.");
						return true;
					}
					p.sendMessage("�cUse /tell <jogador> <mensagem>.");
					return true;
				}
				if (args.length >= 2) {
					Player alvo = Bukkit.getPlayer(args[0]);
					if (alvo == null) {
						p.sendMessage("�cEste jogador se encontra offline!");
						return true;
					}
					if (alvo.getName().equals(p.getName())) {
						p.sendMessage("�cEste jogador � voc�!");
						return true;
					}
					Toggle t = Main.instance.cache.get(alvo.getName());
					if(!t.isTell()) {
						p.sendMessage("�cEste jogador est� com o tell desativado.");
						return true;
					}
					String mensagem = getMensagem(args);
					way(p, "�eMensagem para �f" + alvo.getName() + "�7: " + mensagem,
							new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("�6Clique para responder.").create()),
							new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + alvo.getName() + " "));
					way(alvo, "�eMensagem de �f" + p.getName() + "�7: " + mensagem,
							new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("�6Clique para responder.").create()),
							new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + p.getName() + " "));
					responder.put(p, alvo);

					return true;
				}
			}
		}
		return true;
	}

	public static void way(Player p, String message, HoverEvent hevent, ClickEvent cevent) {
		TextComponent comp = new TextComponent("");
		Stream.of(TextComponent.fromLegacyText(message)).forEach(c -> comp.addExtra(c));
		comp.setHoverEvent(hevent);
		comp.setClickEvent(cevent);

		p.spigot().sendMessage(comp);
	}

}
