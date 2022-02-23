package com.lucas.specterchat.chats;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import com.lucas.specterchat.Main;
import net.hubcoins.CoinsAPI;
import net.hubpunir.listeners.JoinEvents;
import net.hubpunir.object.Punir;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Local implements Listener {

	public static String getMensagem(Player p, String msg) {
		if (!p.hasPermission("hubchat.cor")) {
			return msg;
		} else {
			return msg.replace("&a", "�a").replace("&b", "�b").replace("&c", "�c").replace("&d", "�d")
					.replace("&e", "�e").replace("&f", "�f").replace("&0", "�0").replace("&1", "�1").replace("&2", "�2")
					.replace("&3", "�3").replace("&4", "�4").replace("&5", "�5").replace("&6", "�6").replace("&7", "�7")
					.replace("&8", "�8").replace("&9", "�9");
		}
	}

	public String getFac(Player p) {
		MPlayer mp = MPlayer.get(p);
		if (mp.hasFaction()) {
			return "�7[" + mp.getRole().getPrefix() + mp.getFactionName() + "] ";
		} else {
			return "";
		}
	}

	public String getMagnataTag(Player p) {
		String magnata = CoinsAPI.getNomes().get(0);
		if (p.getName().equals(magnata)) {
			return "�2[$] ";
		} else {
			return "";
		}
	}

	@EventHandler
	public void aoEnviar(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		int range = 20;

		if (e.isCancelled()) {
			e.setCancelled(true);
			return;
		}

		e.setCancelled(true);
		if(net.hublogin.Main.getInstance().preLogin.contains(p.getName())) {
			return;
		}
		if (Main.getInstance().status.equals("MUTADO")) {
			if (!p.hasPermission("hubchat.ignorarchatmute")) {
				p.sendMessage("�cO chat se encontra mutado.");
				return;
			}
		}
		if (net.hubpunir.Main.getInstance().cache.containsKey(p.getName())) {
			Punir pe = net.hubpunir.Main.getInstance().cache.get(p.getName());
			long time = pe.getTempo();
			if (System.currentTimeMillis() >= time) {
				p.sendMessage(" ");
				p.sendMessage("�cO tempo de sua puni��o acabou! J� pode falar no chat.");
				p.sendMessage(" ");
				net.hubpunir.Main.getInstance().cache.remove(p.getName());
			}else{
				long dif = time - System.currentTimeMillis();
				p.sendMessage(" ");
				p.sendMessage("�c�lHUB NETWORK");
				p.sendMessage("�cVoc� se encontra mutado.");
				p.sendMessage(" ");
				p.sendMessage("�cAutor: �f" + pe.getAutor());
				p.sendMessage("�cMotivo: �f" + pe.getMotivo());
				p.sendMessage("�cProva: �f" + pe.getProva());
				p.sendMessage("");
				p.sendMessage("�cVoc� sera desmutado em �f" + JoinEvents.getTempo(dif) + "�c.");
				p.sendMessage(" ");
			}
			return;
		}
		MPlayer mp = MPlayer.get(p);
		Faction f = mp.getFaction();
		String prefix = PermissionsEx.getUser(p).getPrefix().replace("&", "�");
		
		ArrayList<ChatObject> list = new ArrayList<>();
		list.add(new ChatObject("�e[l]",
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("�eChat local.").create())));
		list.add(new ChatObject(" ", null));
		list.add(new ChatObject(getMagnataTag(p), new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("�2Jogador mais rico do servidor.").create())));
		list.add(new ChatObject(getFac(p),
				new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder("�6Nome: �7" + f.getName() + "\n�6Terras: �7" + f.getLandCount()
								+ "\n�6Poder: �7" + f.getPowerRounded() + "/" + f.getPowerMaxRounded()
								+ "\n�6Membros: �7" + f.getMPlayers().size()).create())));
		list.add(new ChatObject(prefix, null));
		list.add(new ChatObject("�f" + p.getName(), new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("�6Nome: �7" + p.getName() + "\n�6Poder�7: " + mp.getPowerRounded() + "/"
						+ mp.getPowerMaxRounded() + "\n�6Coins�7: " + CoinsAPI.format(CoinsAPI.getCoins(p.getName())))
								.create())));
		list.add(new ChatObject(": �e" + getMensagem(p, e.getMessage()), null));

		if (Bukkit.getOnlinePlayers().size() == 1) {
			GComando.way(p, p, list);
			p.sendMessage("�eSem jogadores por perto para lhe ouvir.");
			return;
		}
		GComando.way(p, p, list);
		String s = "S";
		for (Player todos : Bukkit.getOnlinePlayers()) {
			if (todos.getWorld().getName().equalsIgnoreCase(p.getWorld().getName())) {
				if (todos.getLocation().distance(p.getLocation()) <= range) {
					if (!todos.getName().equalsIgnoreCase(p.getName())) {
						GComando.way(p, todos, list);
						if (s.equalsIgnoreCase("S")) {
							s = "N";
						}
					}
				}
			}
		}
		if (s.equalsIgnoreCase("S")) {
			p.sendMessage("�eSem jogadores por perto para lhe ouvir.");
			return;
		}
	}

}
