package com.lucas.specterchat.chats;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import com.lucas.specterchat.Main;
import net.hubcoins.CoinsAPI;
import net.hubpunir.listeners.JoinEvents;
import net.hubpunir.object.Punir;
import net.hubtoggle.object.Toggle;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class GComando implements CommandExecutor {

	public static String getMensagem(Player p, String[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		if (!p.hasPermission("hubchat.cor")) {
			return sb.toString();
		} else {
			return sb.toString().replace("&a", "�a").replace("&b", "�b").replace("&c", "�c").replace("&d", "�d")
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

	public HashMap<String, Long> cd = new HashMap<>();
	DecimalFormat df = new DecimalFormat("#.00");

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lb, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			MPlayer mp = MPlayer.get(p);
			if (cmd.getName().equalsIgnoreCase("g")) {
				if (args.length == 0) {
					p.sendMessage("�cUse /g <mensagem>.");
					return true;
				}
				if (args.length >= 1) {
					if (Main.getInstance().status.equals("MUTADO")) {
						if (!p.hasPermission("hubchat.ignorarchatmute")) {
							p.sendMessage("�cO chat se encontra mutado.");
							return true;
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
						} else {
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
						return true;
					}

					Faction f = mp.getFaction();
					String prefix = PermissionsEx.getUser(p).getPrefix().replace("&", "�");
					if (cd.containsKey(p.getName())) {
						long time = cd.get(p.getName());
						if (System.currentTimeMillis() <= time) {
							long diferenca = time - System.currentTimeMillis();
							double tempo = (double) diferenca / 1000.0;
							p.sendMessage("�cAguarde �f" + String.valueOf(tempo).substring(0, 3)
									+ "�c para utilizar o chat global novamente.");
						} else {
							cd.remove(p.getName());
							cd.put(p.getName(), System.currentTimeMillis() + 6000);
							ArrayList<ChatObject> list = new ArrayList<>();
							list.add(new ChatObject("�7[g]", new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("�7Chat global.").create())));
							list.add(new ChatObject(" ", null));
							list.add(new ChatObject(getMagnataTag(p), new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("�2Jogador mais rico do servidor.").create())));
							list.add(new ChatObject(getFac(p), new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("�6Nome: �7" + f.getName() + "\n�6Terras: �7"
											+ f.getLandCount() + "\n�6Poder: �7" + f.getPowerRounded() + "/"
											+ f.getPowerMaxRounded() + "\n�6Membros: �7" + f.getMPlayers().size())
													.create())));
							list.add(new ChatObject(prefix, null));
							list.add(new ChatObject("�f" + p.getName(), new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("�6Nome: �7" + p.getName() + "\n�6Poder�7: "
											+ mp.getPowerRounded() + "/" + mp.getPowerMaxRounded() + "\n�6Coins�7: "
											+ CoinsAPI.format(CoinsAPI.getCoins(p.getName()))).create())));
							list.add(new ChatObject(": �7" + getMensagem(p, args), null));

							Toggle tt = net.hubtoggle.Main.instance.cache.get(p.getName());
							if (!tt.isGlobal()) {
								if (!p.isOp()) {
									way(p, p, list);
									for (Player on : Bukkit.getOnlinePlayers()) {
										Toggle t = net.hubtoggle.Main.instance.cache.get(on.getName());
										if (t.isGlobal()) {
											way(p, on, list);
										}
									}
								} else {
									for (Player on : Bukkit.getOnlinePlayers()) {
										way(p, on, list);
									}
								}
							} else {
								if (!p.isOp()) {
									for (Player on : Bukkit.getOnlinePlayers()) {
										Toggle t = net.hubtoggle.Main.instance.cache.get(on.getName());
										if (t.isGlobal()) {
											way(p, on, list);
										} else {
											if (p.hasPermission("hubchat.staffmode")) {
												way(p, on, list);
											}
										}
									}
								} else {
									for (Player on : Bukkit.getOnlinePlayers()) {
										way(p, on, list);
									}
								}
							}
						}
						return true;
					}
					if (!p.hasPermission("hubchat.burlarcd")) {
						cd.put(p.getName(), System.currentTimeMillis() + 6000);
					}
					ArrayList<ChatObject> list = new ArrayList<>();
					list.add(new ChatObject("�7[g]", new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("�7Chat global.").create())));
					list.add(new ChatObject(" ", null));
					list.add(new ChatObject(getMagnataTag(p), new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("�2Jogador mais rico do servidor.").create())));
					list.add(new ChatObject(getFac(p), new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("�6Nome: �7" + f.getName() + "\n�6Terras: �7" + f.getLandCount()
									+ "\n�6Poder: �7" + f.getPowerRounded() + "/" + f.getPowerMaxRounded()
									+ "\n�6Membros: �7" + f.getMPlayers().size()).create())));
					list.add(new ChatObject(prefix, null));
					list.add(new ChatObject("�f" + p.getName(),
							new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("�6Nome: �7" + p.getName() + "\n�6Poder�7: "
											+ mp.getPowerRounded() + "/" + mp.getPowerMaxRounded() + "\n�6Coins�7: "
											+ CoinsAPI.format(CoinsAPI.getCoins(p.getName()))).create())));
					list.add(new ChatObject(": �7" + getMensagem(p, args), null));
					Toggle tt = net.hubtoggle.Main.instance.cache.get(p.getName());
					if (!tt.isGlobal()) {
						if (!p.isOp()) {
							way(p, p, list);
							for (Player on : Bukkit.getOnlinePlayers()) {
								Toggle t = net.hubtoggle.Main.instance.cache.get(on.getName());
								if (t.isGlobal()) {
									way(p, on, list);
								}
							}
						} else {
							for (Player on : Bukkit.getOnlinePlayers()) {
								way(p, on, list);
							}
						}
					} else {
						if (!p.isOp()) {
							for (Player on : Bukkit.getOnlinePlayers()) {
								Toggle t = net.hubtoggle.Main.instance.cache.get(on.getName());
								if (t.isGlobal()) {
									way(p, on, list);
								} else {
									if (p.hasPermission("hubchat.staffmode")) {
										way(p, on, list);
									}
								}
							}
						} else {
							for (Player on : Bukkit.getOnlinePlayers()) {
								way(p, on, list);
							}
						}
					}
				}
			}
		}
		return true;

	}

	public static void way(Player jogador, Player p, List<ChatObject> obj) {
		ArrayList<TextComponent> list = new ArrayList<>();
		for (ChatObject co : obj) {
			TextComponent c = new TextComponent(TextComponent.fromLegacyText(co.getMensagem()));
			c.setHoverEvent(co.getEvent());
			list.add(c);
		}
		if (jogador.isOp()) {
			p.spigot().sendMessage(new TextComponent(TextComponent.fromLegacyText(" ")));
			p.spigot().sendMessage(list.toArray(new TextComponent[list.size()]));
			p.spigot().sendMessage(new TextComponent(TextComponent.fromLegacyText(" ")));
		} else {
			p.spigot().sendMessage(list.toArray(new TextComponent[list.size()]));
		}
	}

}
