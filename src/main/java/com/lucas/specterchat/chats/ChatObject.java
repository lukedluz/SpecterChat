package com.lucas.specterchat.chats;

import net.md_5.bungee.api.chat.HoverEvent;

public class ChatObject {

	private String mensagem;
	private HoverEvent event;
	public ChatObject(String mensagem, HoverEvent event) {
		super();
		this.setMensagem(mensagem);
		this.setEvent(event);
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public HoverEvent getEvent() {
		return event;
	}
	public void setEvent(HoverEvent event) {
		this.event = event;
	}
	
	
	
}
