package fr.free.rocheteau.jerome.dunamis.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import fr.free.rocheteau.jerome.dunamis.Dunamis;

public class FrameListener extends WindowAdapter {
	
	private Dunamis dunamis;
	
	public void setDunamis(Dunamis dunamis) {
		this.dunamis = dunamis;
	}
	
	private Dunamis getDunamis() {
		return this.dunamis;
	}
	
	public void windowClosing(WindowEvent event) {
		this.getDunamis().quit();
	 }
	
}
